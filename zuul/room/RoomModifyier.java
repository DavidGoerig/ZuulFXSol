package zuul.room;
import zuul.Game;
import zuul.Item;

import java.util.*;
import java.util.function.Predicate;

public class RoomModifyier {
    // The aim is to use behavior parametization, with lambdas, so we can easily add new room modifyer

//    Faire un filterRoom (avec une predicate qui filtre les books)

  //      Faire un add to rooms avec le retour de filterRoom qui prend que celle SANS objets, et on y ajoute un objet

    // faire add object to maps
    // Delete room from map in allrooms

    /**
     * With behaviour parameterization, it will be easy to add new filter and add new Room modifyer
     * @param roomList
     * @param p
     * @return
     */

    private static HashMap<String, Room> filter(HashMap<String, Room> roomList, Predicate p) {
        HashMap<String, Room> result = new HashMap<>();
        for (Map.Entry<String, Room> stringRoomEntry : roomList.entrySet()) {
            Map.Entry entry = stringRoomEntry;
            if (p.test(entry.getValue())) {
                result.put(entry.getKey().toString(), (Room) entry.getValue());
            }

        }
        return result;
    }

    private HashMap<String, Room> filterRoomWithoutExit(HashMap<String, Room> allRooms) {
        return filter(allRooms, new Predicate<Room>(){
            public boolean test(Room a) {
                String[] directions = {"north", "east", "south", "west"};
                for (String dir: directions) {
                    if (a.getExit(dir) != null) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    public  HashMap<String, Room> newItemAllRoomWithoutExits(HashMap<String, Room> allRooms, String itemDesc, int itemWeight) {
        HashMap<String, Room> roomWhithoutExits = filterRoomWithoutExit(allRooms);
        this.addItemInRooms(allRooms, roomWhithoutExits, itemDesc, itemWeight);
        return allRooms;
    }

    private void addItemInRooms(HashMap<String, Room> allRooms, HashMap<String, Room> roomWhithoutExits, String itemDesc, int itemWeight) {
        for (final String key : roomWhithoutExits.keySet()) {
            if (allRooms.containsKey(key)) {
                allRooms.get(key).addItem(itemDesc, itemWeight);
            }
        }
    }

    public  HashMap<String, Room> removeAllWithoutExits(HashMap<String, Room> allRooms) {
        HashMap<String, Room> roomWhithoutExits = filterRoomWithoutExit(allRooms);
        this.removeRooms(allRooms, roomWhithoutExits);
        return allRooms;
    }

    private void removeConnection(HashMap<String, Room> map, String key) {
        String[] directions = {"north", "east", "south", "west"};
        for (Map.Entry<String, Room> stringRoomEntry : map.entrySet()) {
            Map.Entry entry = stringRoomEntry;

            for (String dir: directions) {
                if (((Room) entry.getValue()).getExit(dir) != null &&
                        ((Room) entry.getValue()).getExit(dir).getName().equals(key)){
                    ((Room) entry.getValue()).removeExit(dir);
                }
            }
        }
    }

    private void removeRooms(HashMap<String, Room> map, HashMap<String, Room> toRemove) {
        for (final String key : toRemove.keySet()) {
            if (map.containsKey(key)) {
                map.remove(key);
                removeConnection(map, key);
            }
        }
    }

    public  HashMap<String, Room> removeAllWithoutItems(HashMap<String, Room> allRooms) {
        HashMap<String, Room> roomWithoutItems = filter(allRooms, new Predicate<Room>(){
            public boolean test(Room a) {
                return !a.containsItem();
            }
        });
        this.removeRooms(allRooms, roomWithoutItems);
        return allRooms;
    }
}