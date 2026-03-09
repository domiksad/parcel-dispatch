package domiksad.parcel_dispatch.domain.model.action;

import domiksad.parcel_dispatch.domain.model.parcel.Parcel;

public class ParcelCheckedIn extends ParcelAction{
    public ParcelCheckedIn(Long ID, String DESC, Parcel parcel) {
        super(ID, DESC, parcel);
    }
}
