package domiksad.parcel_dispatch.domain.model.action;

import domiksad.parcel_dispatch.domain.model.parcel.Parcel;

public class ParcelAction extends Action{
    public final Parcel parcel;

    public ParcelAction(Long ID, String DESC, Parcel parcel) {
        super(ID, DESC);
        this.parcel = parcel;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, TIME: %s, Parcel uuid: %s, DESC: %s  ", ID, TIME, parcel.getID(), DESC);
    }
}
