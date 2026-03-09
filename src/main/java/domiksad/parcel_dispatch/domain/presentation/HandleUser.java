package domiksad.parcel_dispatch.domain.presentation;

import domiksad.parcel_dispatch.domain.model.History;
import domiksad.parcel_dispatch.domain.model.human.Courier;
import domiksad.parcel_dispatch.domain.model.human.Person;
import domiksad.parcel_dispatch.domain.model.parcel.Parcel;
import domiksad.parcel_dispatch.domain.model.parcel.Priority;
import domiksad.parcel_dispatch.domain.model.parcel.Status;
import domiksad.parcel_dispatch.domain.model.sortingRoom.SortingRoom;
import domiksad.parcel_dispatch.domain.model.statistics.CityStatistics;
import domiksad.parcel_dispatch.domain.model.statistics.CourierStatistics;
import domiksad.parcel_dispatch.domain.model.statistics.ParcelStatistics;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleUser { // ANULOWANIE I STATYSTYKI
    private Scanner scanner;

    public HandleUser(Scanner scanner){
        this.scanner = scanner;
    }

    private final ArrayList<Person> persons = new ArrayList<>();
    private final ArrayList<Courier> couriers = new ArrayList<>();
    private Long latestId = 0L;
    private final ArrayList<Parcel> parcels = new ArrayList<>();

    SortingRoom sortingRoom = new SortingRoom();

    public static final Map<String, String> help = new HashMap<>();

    {
        help.put("help", "help <command> - display help message");

        help.put("create", "create <person | courier | parcel> *params* - create new <person | courier | parcel>");
        help.put("create person", "create person \"full name\" \"address\"");
        help.put("create courier", "create courier \"full name\" <parcelLimitPerDay>");
        help.put("create parcel", "create parcel <index of a sender> <index of a receiver> \"delivery city\" <weight> priority<high | low>");

        help.put("list", "list <person | courier | parcel | sortingRoom | all> - list all <person | courier | parcel | sortingRoom | all> objects");

        help.put("process", """
                process parcel <parcel index | parcel uuid> - moves parcel to sorting room
                process sortingRoom <courier id | index> - gives parcel to courier
                process courier <id> - process all packages that courier has
                process courier <id> <parcel index | parcel uuid> - process one package that courier has""");

        help.put("find", "find <parcel uuid> - gives information about parcel");

        help.put("delete", "delete <index> - deletes unprocessed parcel by index");

        help.put("cancel", "cancel <parcel uuid> - cancels parcel");

        help.put("stats", "stats - prints out stats");

        help.put("log", "log <print, dump> - displays or dumps logs");
    }

    public static ArrayList<String> parseCommand(String input){ // ai
        ArrayList<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"([^\"]+)\"|\\S+");
        Matcher matcher = pattern.matcher(input);

        while(matcher.find()){
            if(matcher.group(1) != null){
                tokens.add(matcher.group(1)); // Token in ""
            } else {
                tokens.add(matcher.group()); // Normal token
            }
        }

        return tokens;
    }

    /**
     * Returns true if user answers "y" or "yes".
     */
    public boolean makeSure(){
        String input = scanner.nextLine().trim();
        return input != null && (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"));
    }

    public String parcelInfo(Parcel parcel){
        return String.format("Parcel UUID: %s\nSender: %s\nReceiver: %s\nDelivery city: %s\nWeight: %.2f\nStatus: %s\nPriority: %s",
                parcel.getID(),
                parcel.getSender().toString(),
                parcel.getReceiver().toString(),
                parcel.getDeliveryCity(),
                parcel.getWeight(),
                parcel.getStatus(),
                parcel.getPriority());
    }

    public void run(){
        String input = "";

        while(true){
            System.out.print("> ");

            if(!scanner.hasNextLine()) break; // Stop if user does ctrl+c or ctrl+d

            input = scanner.nextLine();
            ArrayList<String> result = parseCommand(input);

            if(result.isEmpty()) continue;

            String command = result.getFirst();
            ArrayList<String> args = new ArrayList<>();

            if(result.size() > 1) args = new ArrayList<>(result.subList(1, result.size())); // get everything except command

            switch(command){
                case "help" -> handleHelp(args);
                case "create" -> handleCreate(args);
                case "list" -> handleList(args);
                case "process" -> handleProcess(args);
                case "find" -> handleFind(args);
                case "cancel" -> handleCancel(args);
                case "delete" -> handleDelete(args);
                case "stats" -> handleStats(args);
                case "log" -> handleLog(args);
            }
        }

        System.out.println("Exiting");
        scanner.close();
    }

    private void handleHelp(ArrayList<String> args){
        if(args.isEmpty()){
            System.out.println("No argument provided. Try help \"help\"");
        } else {
            String joined = String.join(" ", args);
            System.out.println(help.getOrDefault(joined, "Command not found: " + joined));
        }
    }

    private void handleCreate(ArrayList<String> args){
        if(args.isEmpty()){
            System.out.println("No argument provided. Try help \"create\"");
            return;
        }

        String type = args.getFirst();

        switch(type){
            case "person":
                if(args.size() == 3){
                    int index = persons.size();
                    persons.add(new Person(args.get(1), args.get(2)));
                    System.out.println("Person added at index: " + index);
                } else {
                    System.out.println("Argument count is invalid: " + args.size() + " (expected 3)");
                }
                break;

            case "courier":
                if(args.size() == 3){
                    int number;
                    try {
                        number = Integer.parseInt(args.get(2));
                    } catch(NumberFormatException e){
                        System.out.println("Expected integer in third argument");
                        break;
                    }
                    couriers.add(new Courier(latestId, args.get(1), number));
                    System.out.println("Courier added (ID: " + latestId + ")");
                    latestId++;
                } else {
                    System.out.println("Argument count is invalid: " + args.size() + " (expected 3)");
                }
                break;

            case "parcel":
                if(args.size() == 6){
                    int senderId;
                    int receiverId;
                    float weight;
                    try {
                        senderId = Integer.parseInt(args.get(1));
                        receiverId = Integer.parseInt(args.get(2));
                        weight = Float.parseFloat(args.get(4));
                    } catch(NumberFormatException e){
                        System.out.println("Expected number in third, forth and sixth argument");
                        break;
                    }

                    Person sender;
                    try {
                        sender = persons.get(senderId);
                    } catch(IndexOutOfBoundsException e){
                        System.out.println("No person with index: " + senderId);
                        break;
                    }

                    Person receiver;
                    try {
                        receiver = persons.get(receiverId);
                    } catch(IndexOutOfBoundsException e){
                        System.out.println("No person with index: " + receiverId);
                        break;
                    }

                    String deliveryCity = args.get(3);
                    Priority priority;
                    switch(args.get(5).toLowerCase()){
                        case "high" -> priority = Priority.HIGH;
                        case "low" -> priority = Priority.LOW;
                        default -> {
                            System.out.println("No priority: " + args.get(5).toLowerCase());
                            return;
                        }
                    }
                    Parcel parcel = new Parcel(sender, receiver, deliveryCity, weight, priority);

                    if(History.isDuplicate(parcel)){
                        System.out.println("Parcel with these details already exists. Do you wish to proceed anyways? (y)es | (n)o");
                        if(!makeSure()) break;
                    }
                    System.out.println("Parcel details:\n" + parcelInfo(parcel) + "\nCreate parcel? (y)es | (n)o");
                    if(makeSure()){
                        parcel.create();
                        parcels.add(parcel);
                        System.out.println("Parcel created with UUID: " + parcel.getID());
                    }
                } else {
                    System.out.println("Argument count is invalid: " + args.size() + " (expected 6)");
                }
                break;

            default:
                System.out.println("No valid type: " + type + ". Try help \"create\"");
                break;
        }
    }

    private void handleList(ArrayList<String> args){
        if(args.isEmpty()){
            System.out.println("No argument provided. Try help \"list\"");
            return;
        }
        String type = args.getFirst();
        switch (type){
            case "all":
            case "person":
                System.out.println("Persons list:");
                for(int i = 0; i < persons.size(); i++){
                    Person person = persons.get(i);
                    System.out.format("Index: %d %s %s\n", i, person.getFullName(), person.getAdress());
                }
                System.out.println();
                if(!type.equals("all")) break;

            case "courier":
                System.out.println("Couriers list:");
                for(int i = 0; i < couriers.size(); i++){
                    Courier courier = couriers.get(i);
                    System.out.format("ID: %d %s Parcel count: %d/%d\n", courier.ID, courier.getFullName(), courier.getCurrentParcels().size(), courier.getParcelLimitPerDay());
                    for(var p : courier.getCurrentParcels()){
                        System.out.println("Parcel index: " + i + "\n" + parcelInfo(p) + "\n");
                    }
                }
                System.out.println();
                if(!type.equals("all")) break;

            case "parcel":
                System.out.println("Parcel list:");
                for(int i = 0; i < parcels.size(); i++){
                    Parcel parcel = parcels.get(i);
                    System.out.println("Parcel index: " + i + "\n" + parcelInfo(parcel) + "\n");
                }
                System.out.println();
                if(!type.equals("all")) break;

            case "sortingRoom":
                System.out.println("Sorting room:");
                for(var parcel : sortingRoom.getParcelList()){
                    System.out.println(parcelInfo(parcel) + "\n");
                }
                break;
            default:
                System.out.println("No valid type: " + type + ". Try help \"list\"");
                break;
        }
    }

    private void handleProcess(ArrayList<String> args){
        if(args.isEmpty()){
            System.out.println("No argument provided. Try help \"process\"");
            return;
        }
        if(args.size() < 2){
            System.out.println("Argument count is invalid: " + args.size() + " (expected 2 or 3)");
            return;
        }

        class ParcelIdentifier{
            public static Parcel getParcel(String identifier, ArrayList<Parcel> parcels){
                Parcel parcel;
                try {
                    UUID uuid = UUID.fromString(identifier);
                    for(var p : parcels){
                        if(p.getID().equals(uuid)){
                            parcel = p;
                            return parcel;
                        }
                    }
                } catch (IllegalArgumentException _){
                    try {
                        int index = Integer.parseInt(identifier);
                        parcel = parcels.get(index);
                        return parcel;
                    } catch (NumberFormatException e){
                        System.out.println("Parcel index / uuid not recognized");
                        return null;
                    } catch (IndexOutOfBoundsException e){
                        System.out.println("Parcel index out of bounds");
                        return null;
                    }
                }
                System.out.println("Parcel not found");
                return null;
            }
        }
        class CourierIdentifier{
            public static Courier getCourier(String identifier, ArrayList<Courier> couriers){
                Courier courier;
                try{
                    int id = Integer.parseInt(identifier);
                    courier = couriers.stream()
                            .filter(c -> c.ID == id)
                            .findAny()
                            .orElseThrow(() -> new RuntimeException("Courier not found"));
                } catch (NumberFormatException e) {
                    System.out.println("Expected number");
                    return null;
                } catch (RuntimeException e){
                    System.out.println("No courier with ID: " + identifier);
                    return null;
                }
                return courier;
            }
        }

        String type = args.get(0);
        String identifier = args.get(1);
        Parcel parcel;
        Courier courier;
        switch(type){
            case "parcel":
                parcel = ParcelIdentifier.getParcel(identifier, parcels);
                if(parcel == null) return;

                System.out.println(parcelInfo(parcel) + "\nProcess parcel? (y)es | (n)o");

                if(makeSure()){
                    sortingRoom.checkInParcel(parcel);
                    parcels.remove(parcel);
                }
                break;

            case "sortingRoom":
                courier = CourierIdentifier.getCourier(identifier, couriers);
                if(courier == null) return;

                sortingRoom.assignParcels(courier);
                System.out.println("Parcels assigned to courier");
                break;

            case "courier":
                courier = CourierIdentifier.getCourier(identifier, couriers);
                if(courier == null) return;

                if(args.size() == 3){
                    ArrayList<Parcel> courierParcels = new ArrayList<>(courier.getCurrentParcels());
                    parcel = ParcelIdentifier.getParcel(args.get(2), courierParcels);
                    if(parcel == null) return;

                    int index = courierParcels.indexOf(parcel);
                    courier.deliver(index);
                    System.out.println("Parcel delivered:\n" + parcelInfo(parcel));
                } else {
                    courier.deliverAll();
                    System.out.println("Parcels delivered");
                }
                break;

            default:
                System.out.println("No valid type: " + type + ". Try help \"process\"");
                break;
        }
    }

    private void handleFind(ArrayList<String> args){
        if(args.size() != 1){
            System.out.println("Incorrect argument count (expected 1)");
            return;
        }
        String identifier = args.get(0);
        try{
            UUID uuid = UUID.fromString(identifier);
            Parcel parcel = History.getParcelMap().get(uuid);
            if(parcel == null) throw new RuntimeException("Parcel not found");
            System.out.println(parcelInfo(parcel));
        } catch (IllegalArgumentException e){
            System.out.println("Parcel uuid not recognized");
            return;
        } catch (RuntimeException e){
            System.out.println("Parcel not found");
            return;
        }
    }

    private void handleCancel(ArrayList<String> args){
        if(args.size() != 1){
            System.out.println("Incorrect argument count (expected 1)");
            return;
        }

        String identifier = args.get(0);
        try{
            UUID uuid = UUID.fromString(identifier);
            Parcel parcel = History.getParcelMap().get(uuid);
            if(parcel == null) throw new RuntimeException("Parcel not found");

            if(parcel.getStatus() == Status.DELIVERED){
                System.out.println("Cant cancel delivered parcel");
                return;
            };

            parcel.updateStatus(Status.CANCELED);

            parcels.remove(parcel);
            sortingRoom.getParcelList().remove(parcel);
            for(Courier c : couriers){
                c.getCurrentParcels().remove(parcel);
            }
            System.out.println("Parcel canceled");

        } catch (IllegalArgumentException e){
            System.out.println("Parcel uuid not recognized");
            return;
        } catch (RuntimeException e){
            System.out.println("Parcel not found");
            return;
        }
    }

    private void handleDelete(ArrayList<String> args){
        if(args.size() != 1){
            System.out.println("Incorrect argument count (expected 1)");
            return;
        }
        String identifier = args.get(0);
        try {
            int index = Integer.parseInt(identifier);
            Parcel parcel = parcels.get(index);
            System.out.println(parcelInfo(parcel)+"\nAre you sure you want to delete this parcel? (y)es | (n)o");

            if(!makeSure()) return;
            parcels.remove(index);
            History.deleteParcelInfo(parcel);
        } catch (NumberFormatException e){
            System.out.println("Expected number");
            return;
        } catch (IndexOutOfBoundsException e){
            System.out.println("Parcel index out of bounds");
            return;
        }
    }

    private void handleStats(ArrayList<String> args){
        ParcelStatistics.printStats();
        System.out.println();
        CourierStatistics.printStats();
        System.out.println();
        CityStatistics.printStats();
    }

    private void handleLog(ArrayList<String> args){
        if(args.size() != 1){
            System.out.println("Incorrect argument count (expected 1)");
            return;
        }
        switch(args.get(0).toLowerCase()){
            case "print":
                for(var a : History.getActionList()){
                    System.out.println("ID: " + a.ID + " TIME: " + a.TIME + " DESC: " + a.DESC);
                }
                break;

            case "dump":
                System.out.println(History.getActionList().toString());
                break;

            default:
                System.out.println("No option: " + args.get(0).toLowerCase());
                break;
        }
    }
}
