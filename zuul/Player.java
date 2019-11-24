package zuul;

import zuul.room.Room;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A Player class. The player knows all about how this player responds to commands.
 * Arguably some of these actions might be better places in a game specific class in
 * zuul.mygame.
 * @author rej
 */
public class Player {
    private String name;
    private Room currentRoom;
    
    /** A map of items the player has */
    final private Map<String, Item> items;
    /** the total weight of these items */
    private int totalWeight;
    // An alternative would be an Inventory class that encapsulates the Map and the totalWeight
    
    /** the max weight a player can hold. */
    private static final int MAX_WEIGHT = 10;
    // Could have per-player weights
 
    
    /**
     * Create a player
     * @param name name of the player
     * @param room the room the player is created in
     */
    public Player(String name, Room room) {
        this.name = name;
        currentRoom = room;
        items = new HashMap<>();
    }

    /** @return the name */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    /** @return the currentRoom */
    public Room getCurrentRoom() { return currentRoom; }

    /** @param currentRoom the currentRoom to set */
    public void setCurrentRoom(Room currentRoom) { this.currentRoom = currentRoom; }
    
    /** @return a full description of the room and its contents */
    public List<String> getDetails() { return getCurrentRoom().getDetails(); }

    /** @return the max weight the player can carry */
    public int getMaxWeight() { return MAX_WEIGHT; }
    
    /**
     * Is something too heavy to hold
     * @param item the item
     * @return true iff too heavy
     */
    public boolean tooHeavy(Item item) { return (item.getWeight() + totalWeight > getMaxWeight()); }

    /**
     * @param desc the name of the item
     * @return true iff the player has this item
     */
    public boolean hasItem(String desc) { return items.containsKey(desc); }
    
     /*-------- Actions ------------*/ 
    // If these are generic actions for all conceivable games, then this is the right
    // place for these methods. If not, then we should put them in a zuul.mygame.player class
   
    /**
     * Take an item from a room. Check whether it is in the room and it is not too heavy
     * @param desc the name of the item
     */
    public void take(String desc) {
        if (!getCurrentRoom().containsItem(desc)) {
            // The item is not in the room
            Game.out.println(desc + " " + Game.messages.getString("zuul/room")); // is not in the room"
            return;
        }
        Item item = getCurrentRoom().getItem(desc);
        if (tooHeavy(item)) {
            // The player is carrying too much
            Game.out.println(desc + " " + Game.messages.getString("heavy")); // is too heavy
            return;
        }

        item = getCurrentRoom().removeItem(desc);
        items.put(desc, item);
        totalWeight += item.getWeight();
    }

    /**
     * Put an item the player has into the current room
     * @param desc the name of the item - check if the player is carrying it
     */
    public void drop(String desc) {
        if (!hasItem(desc)) {
            Game.out.println(Game.messages.getString("dontHave") + " " + desc); // You don't have the...
            return;
        }
        Item item = items.remove(desc);
        totalWeight -= item.getWeight();
        currentRoom.addItem(desc, item);
    }

    /**
     * Give an item to a character
     * @param desc the name of the item - check if the player is carrying it
     * @param whom the character - check if the character is in the current room
     */
    public void give(String desc, String whom) {
        if (!currentRoom.hasCharacter(whom)) {
            // cannot give it if the character is not here
            Game.out.println(whom + " " + Game.messages.getString("zuul/room")); // is not in the room
            return;
        }
        if (!items.containsKey(desc)) {
            Game.out.println(Game.messages.getString("zuul/room") + " " + desc); // You don't have the...
            return;
        }
        Item item = items.remove(desc);
        totalWeight -= item.getWeight();
    }

    /**
     * Look around the current room, giving the user some info
     */
    public void look() {
        for (String str : getCurrentRoom().getDetails())
        	Game.out.println(str);
    }

    /**
     * Go to another room
     * @param direction the name of the door to use - check the door exists
     */
    public boolean goRoom(String direction) {
        // Try to leave current room.
        Room nextRoom = getCurrentRoom().getExit(direction);
        if (nextRoom == null) {
            Game.out.println(Game.messages.getString("door")); // There is no door!
            return false;
        } else {
            setCurrentRoom(nextRoom);
            look();
        }
        return true;
    }
}
