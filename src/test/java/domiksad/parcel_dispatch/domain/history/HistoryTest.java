package domiksad.parcel_dispatch.domain.history;

import domiksad.parcel_dispatch.domain.model.History;
import domiksad.parcel_dispatch.domain.model.action.*;
import domiksad.parcel_dispatch.domain.model.human.Courier;
import domiksad.parcel_dispatch.domain.model.human.Person;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;
import domiksad.parcel_dispatch.domain.model.parcel.Priority;
import domiksad.parcel_dispatch.domain.model.sortingRoom.SortingRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryTest {
    @BeforeEach
    void clearHistory() {
        History.clear();
    }

    @Test
    void testHistory() {
        Person h1 = new Person("Adam", "Zielona 5");
        Person h2 = new Person("Beata", "Polna 2");

        Parcel p1 = new Parcel(h1, h2, "Radom", 12.3f, Priority.LOW).create();

        assertEquals(History.getParcelMap().get(p1.getID()), p1); // check if parcel is in global parcel list

        Action action = History.getActionList().get(0); // check if created message is there
        assertTrue(action instanceof ParcelAction);
        ParcelAction pa = (ParcelAction) action;
        assertEquals(pa.parcel, p1);

        Courier c1 = new Courier(0L, "Kamil", 2);

        SortingRoom s1 = new SortingRoom();

        s1.checkInParcel(p1);
        Action action2 = History.getActionList().get(1); // check if checked in message is there
        assertTrue(action2 instanceof ParcelCheckedIn);

        s1.assignParcels(c1);
        Action action3 = History.getActionList().get(2); // check if parcel assigned message is there
        assertTrue(action3 instanceof ParcelAssigned);

        c1.deliver(0);
        Action action4 = History.getActionList().get(3); // check if parcel delivered message is there
        assertTrue(action4 instanceof ParcelDelivered);
    };

    @Test
    void testDuplicates(){
        Person h1 = new Person("Adam", "Zielona 5");
        Person h2 = new Person("Beata", "Polna 2");

        Person h3 = new Person("Adam", "Zielona 5");
        Person h4 = new Person("Beata", "Polna 2");

        Parcel p1 = new Parcel(h1, h2, "Radom", 12.3f, Priority.LOW).create();
        Parcel p2 = new Parcel(h3, h4, "Radom", 12.3f, Priority.LOW);
        Parcel p3 = new Parcel(h1, h2, "Radom", 14.2f, Priority.LOW);

        assertTrue(History.isDuplicate(p2));
        assertFalse(History.isDuplicate(p3));
    }
}
