package domiksad.parcel_dispatch.domain.model.parcel;

import domiksad.parcel_dispatch.domain.model.History;
import domiksad.parcel_dispatch.domain.model.human.Person;
import domiksad.parcel_dispatch.domain.model.statistics.ParcelStatistics;

import java.sql.Timestamp;
import java.util.EnumMap;
import java.util.UUID;

public class Parcel {
    private UUID ID;
    private Person sender;
    private Person receiver;
    private String deliveryCity;
    private Float weight;
    private Priority priority;
    private Status status = Status.CREATED;
    private Timestamp creationTime;

    public Parcel(Person sender,
                  Person receiver,
                  String deliveryCity,
                  Float weight,
                  Priority PRIORITY) {
        // Should be
        //do {
        //      this.ID = UUID.randomUUID();
        // } while(History.getParcelMap().containsKey(this.ID));
        // But probability of collision is extremely low
        this.ID = UUID.randomUUID();
        this.sender = sender;
        this.receiver = receiver;
        this.deliveryCity = deliveryCity;
        this.weight = weight;
        this.priority = PRIORITY;
        creationTime = new Timestamp(System.currentTimeMillis());
    }

    public Parcel create(){
        History.addParcel(this);
        ParcelStatistics.increment(status);
        return this;
    }

    public UUID getID() {
        return ID;
    }

    public Person getSender() {
        return sender;
    }

    public Person getReceiver() {
        return receiver;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public Float getWeight() {
        return weight;
    }

    public Priority getPriority(){
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public void updateStatus(Status status){
        ParcelStatistics.decrement(this.status);
        ParcelStatistics.increment(status);
        this.status = status;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }
}
