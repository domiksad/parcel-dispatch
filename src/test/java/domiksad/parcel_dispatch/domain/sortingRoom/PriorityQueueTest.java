package domiksad.parcel_dispatch.domain.sortingRoom;

import domiksad.parcel_dispatch.domain.model.human.Courier;
import domiksad.parcel_dispatch.domain.model.human.Person;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;
import domiksad.parcel_dispatch.domain.model.parcel.Priority;
import domiksad.parcel_dispatch.domain.model.parcel.Status;
import domiksad.parcel_dispatch.domain.model.sortingRoom.SortingRoom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityQueueTest {
    @Test
    void testSortingRoomPriorityQueue(){
        Person h1 = new Person("Adam", "Zielona 5");
        Person h2 = new Person("Beata", "Polna 2");

        Parcel p1 = new Parcel(h1, h2, "Radom", 12.3f, Priority.LOW).create();
        Parcel p2 = new Parcel(h1, h2, "Radom", 32.1f, Priority.LOW).create();
        Parcel p3 = new Parcel(h2, h1, "Radom", 24.3f, Priority.HIGH).create();

        Courier c1 = new Courier(0L, "Kamil", 2);

        SortingRoom s1 = new SortingRoom();

        s1.checkInParcel(p1);
        s1.checkInParcel(p2);
        s1.checkInParcel(p3);

        assertEquals(Status.ACCEPTED, p1.getStatus());
        assertEquals(Status.ACCEPTED, p2.getStatus());
        assertEquals(Status.ACCEPTED, p3.getStatus());

        s1.assignParcels(c1);

        assertEquals(Status.DISPATCHED, p3.getStatus());
        assertEquals(Status.DISPATCHED, p1.getStatus()); // Older parcel first
        assertEquals(Status.ACCEPTED, p2.getStatus()); // Check if parcel status changed
    }
}
