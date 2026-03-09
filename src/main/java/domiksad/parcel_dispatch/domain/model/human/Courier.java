package domiksad.parcel_dispatch.domain.model.human;

import domiksad.parcel_dispatch.domain.model.History;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;
import domiksad.parcel_dispatch.domain.model.parcel.Status;
import domiksad.parcel_dispatch.domain.model.statistics.CityStatistics;
import domiksad.parcel_dispatch.domain.model.statistics.CourierStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Courier implements Human{
    public final long ID;
    private String fullName;
    private int parcelLimitPerDay;
    private List<Parcel> currentParcels = new ArrayList<>();

    public Courier(long ID,
                   String fullName,
                   int parcelLimitPerDay) {
        this.ID = ID;
        this.fullName = fullName;
        this.parcelLimitPerDay = parcelLimitPerDay;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    public int getParcelLimitPerDay() {
        return parcelLimitPerDay;
    }

    public List<Parcel> getCurrentParcels() {
        return Collections.unmodifiableList(currentParcels);
    }

    public void giveParcel(Parcel parcel){
        currentParcels.add(parcel);
    }

    public void deliver(int index){
        Parcel parcel = currentParcels.get(index);
        parcel.updateStatus(Status.DELIVERED);
        currentParcels.remove(index);

        CourierStatistics.increment(ID);

        History.parcelDelivered(parcel, "Parcel was successfully delivered");
    }

    public void deliverAll(){
        Iterator<Parcel> it = currentParcels.iterator();
        while(it.hasNext()){
            Parcel parcel = it.next();
            parcel.updateStatus(Status.DELIVERED);
            it.remove();

            CourierStatistics.increment(ID);

            CityStatistics.increment(parcel.getDeliveryCity());

            History.parcelDelivered(parcel, "Parcel was successfully delivered");
        }
    }
}
