package domiksad.parcel_dispatch.domain.model.action;

import domiksad.parcel_dispatch.domain.model.parcel.Parcel;

public class ParcelDelivered extends ParcelAction{
    public ParcelDelivered(Long ID, String DESC, Parcel parcel) {
        super(ID, DESC, parcel);
    }
}
