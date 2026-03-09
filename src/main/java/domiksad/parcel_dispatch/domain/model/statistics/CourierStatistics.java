package domiksad.parcel_dispatch.domain.model.statistics;

import java.util.HashMap;
import java.util.Map;

public class CourierStatistics {
    private static final Map<Long, Integer> deliveredParcels = new HashMap<>();

    public static void increment(long courierId){
        deliveredParcels.put(courierId, deliveredParcels.getOrDefault(courierId, 0) + 1);
    }

    public static void printStats(){
        for(var entry : deliveredParcels.entrySet()){
            System.out.println("Courier ID: " + entry.getKey() + " delivered: " + entry.getValue());
        }
    }
}
