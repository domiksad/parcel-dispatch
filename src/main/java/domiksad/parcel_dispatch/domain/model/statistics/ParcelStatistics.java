package domiksad.parcel_dispatch.domain.model.statistics;

import domiksad.parcel_dispatch.domain.model.parcel.Status;

import java.util.EnumMap;

public class ParcelStatistics {
    private static final EnumMap<Status, Integer> counter = new EnumMap<>(Status.class);

    static {
        for (Status s : Status.values()) {
            counter.put(s, 0);
        }
    }

    public static void increment(Status status){
        counter.put(status, counter.get(status) + 1);
    }

    public static void decrement(Status status){
        counter.put(status, counter.get(status) - 1);
    }

    public static void printStats(){
        for(var entry : counter.entrySet()){
            System.out.println("Status: " + entry.getKey() + " exists: " + entry.getValue());
        }
    }
}
