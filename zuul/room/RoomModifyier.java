package zuul.room;

import java.util.*;
import java.util.function.Predicate;

public class RoomModifyier {
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

    /**
     * filter room without exits
     * @param allRooms all rooms
     * @return room without exits
     */
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

    /**
     * add new item to all room without exits
     * @param allRooms all rooms
     * @param itemDesc item description
     * @param itemWeight item weight
     * @return
     */
    public  HashMap<String, Room> newItemAllRoomWithoutExits(HashMap<String, Room> allRooms, String itemDesc, int itemWeight) {
        HashMap<String, Room> roomWhithoutExits = filterRoomWithoutExit(allRooms);
        this.addItemInRooms(allRooms, roomWhithoutExits, itemDesc, itemWeight);
        return allRooms;
    }

    /**
     * add item in all room given in parameter
     * @param allRooms all rooms
     * @param roomWhithoutExits all rooms without exits
     * @param itemDesc item desc
     * @param itemWeight item weight
     */
    private void addItemInRooms(HashMap<String, Room> allRooms, HashMap<String, Room> roomWhithoutExits, String itemDesc, int itemWeight) {
        for (final String key : roomWhithoutExits.keySet()) {
            if (allRooms.containsKey(key)) {
                allRooms.get(key).addItem(itemDesc, itemWeight);
            }
        }
    }

    /**
     * remove all room without exits
     * @param allRooms all rooms
     * @return all room without exis
     */
    public  HashMap<String, Room> removeAllWithoutExits(HashMap<String, Room> allRooms) {
        HashMap<String, Room> roomWhithoutExits = filterRoomWithoutExit(allRooms);
        this.removeRooms(allRooms, roomWhithoutExits);
        return allRooms;
    }

    /**
     * remove connection
     * @param map map
     * @param key key
     */
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

    /**
     * remove rooms
     * @param map map
     * @param toRemove rooms to removes
     */
    private void removeRooms(HashMap<String, Room> map, HashMap<String, Room> toRemove) {
        for (final String key : toRemove.keySet()) {
            if (map.containsKey(key)) {
                map.remove(key);
                removeConnection(map, key);
            }
        }
    }

    /**
     * remove all room without items
     * @param allRooms all rooms
     * @return all rooms without items
     */
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