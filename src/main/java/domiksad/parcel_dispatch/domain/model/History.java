package domiksad.parcel_dispatch.domain.model;

import domiksad.parcel_dispatch.domain.model.action.*;
import domiksad.parcel_dispatch.domain.model.human.Courier;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;

import java.util.*;

// For bigger projects remove static
public class History {
    private static final HashSet<String> duplicateHashSet = new HashSet<>();
    private static final HashMap<UUID, Parcel> parcelMap = new HashMap<>();
    private static final ArrayList<Action> actionList = new ArrayList<>();

    public static void addParcel(Parcel parcel){
        parcelMap.put(parcel.getID(), parcel);
        duplicateHashSet.add(stringifyParcel(parcel));

        long id = actionList.size();
        actionList.add(new ParcelAction(id, "Parcel created", parcel));
    }

    public static void parcelDelivered(Parcel parcel, String desc){
        long id = actionList.size();

        actionList.add(new ParcelDelivered(id, desc, parcel));
    }

    public static void addParcelAssigned(Parcel parcel, Courier courier, String desc){
        long id = actionList.size();

        actionList.add(new ParcelAssigned(id, desc, parcel, courier));
    }

    public static void addParcelCheckedIn(Parcel parcel, String desc){
        long id = actionList.size();

        actionList.add(new ParcelCheckedIn(id, desc, parcel));
    }

    public static Map<UUID, Parcel> getParcelMap(){
        return Collections.unmodifiableMap(parcelMap);
    }

    public static List<Action> getActionList(){
        return Collections.unmodifiableList(actionList);
    }

    private static String stringifyParcel(Parcel parcel){
        return parcel.getSender().toString()
                + parcel.getReceiver().toString()
                + parcel.getDeliveryCity()
                + parcel.getWeight().toString(); // all not random data
    }

    public static boolean isDuplicate(Parcel parcel){
        return duplicateHashSet.contains(stringifyParcel(parcel));
    }

    public static void deleteParcelInfo(Parcel parcel){
        parcelMap.remove(parcel.getID());
        duplicateHashSet.remove(stringifyParcel(parcel));

        long id = actionList.size();
        actionList.add(new Action(id, "Removed parcel from history"));
    }

    public static void clear(){
        duplicateHashSet.clear();
        parcelMap.clear();
        actionList.clear();
    }
}
