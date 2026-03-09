package domiksad.parcel_dispatch.domain.model.action;

import java.sql.Timestamp;

public class Action {
    public final Long ID;
    public final Timestamp TIME;
    public final String DESC;

    public Action(Long ID, String DESC) {
        this.ID = ID;
        this.TIME = new Timestamp(System.currentTimeMillis());
        this.DESC = DESC;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, TIME: %s, DESC: %s", ID, TIME, DESC);
    }
}
