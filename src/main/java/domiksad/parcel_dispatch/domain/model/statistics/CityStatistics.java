package domiksad.parcel_dispatch.domain.model.statistics;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CityStatistics {
    private static final Map<String, Integer> cities = new HashMap<>();
    private static final Map<LocalDate, Set<String>> citiesPerDay = new HashMap<>();

    public static void increment(String city){
        cities.put(city, cities.getOrDefault(city, 0) + 1);
        citiesPerDay.computeIfAbsent(LocalDate.now(), d -> new HashSet<>()).add(city);
    }

    public static void printStats(){
        for(var entry : cities.entrySet()){
            System.out.println("City: " + entry.getKey() + " delivered: " + entry.getValue());
        }
    }
}
