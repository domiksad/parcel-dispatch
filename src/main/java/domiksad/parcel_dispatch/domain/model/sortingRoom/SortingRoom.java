package domiksad.parcel_dispatch.domain.model.sortingRoom;

import domiksad.parcel_dispatch.domain.model.History;
import domiksad.parcel_dispatch.domain.model.human.Courier;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;
import domiksad.parcel_dispatch.domain.model.parcel.Status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SortingRoom {
    private final PriorityQueue<Parcel> queue = new PriorityQueue<>(
            Comparator.comparing(Parcel::getPriority).reversed() // sorts asc so reverse
                    .thenComparing(Parcel::getCreationTime));

    public void assignParcels(Courier courier){
        int remainingLimit = courier.getParcelLimitPerDay() - courier.getCurrentParcels().size();
        for(; remainingLimit > 0 && !queue.isEmpty(); remainingLimit--){
            Parcel parcel = queue.poll();
            parcel.updateStatus(Status.DISPATCHED);
            courier.giveParcel(parcel);

            // log
            History.addParcelAssigned(parcel, courier, "Parcel assigned to courier");
        }
    }

    public void checkInParcel(Parcel parcel){ // ADD CHECKING FOR DUPLICATES
        parcel.updateStatus(Status.ACCEPTED);
        queue.add(parcel);

        History.addParcelCheckedIn(parcel, "Parcel checked in");
    }

    public ArrayList<Parcel> getParcelList(){
        return new ArrayList<>(queue);
    }
}
