package domiksad.parcel_dispatch.domain.model.action;

import domiksad.parcel_dispatch.domain.model.human.Courier;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;

public class ParcelAssigned extends ParcelAction{
    public final Courier courier;
    public ParcelAssigned(Long ID, String DESC, Parcel parcel, Courier courier) {
        super(ID, DESC, parcel);
        this.courier = courier;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, TIME: %s, Parcel uuid: %s, Courier ID: %d, DESC: %s  ", ID, TIME, parcel.getID(), courier.ID, DESC);
    }
}
