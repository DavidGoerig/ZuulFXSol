package zuul.room;

import zuul.BadExitException;
import zuul.Character;
import zuul.Game;
import zuul.Item;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
public class Room 
{
    /** Description of the room */
    final private String description;
    /** Name of the room */
    final private String name;

    public Map<String, Room> getExits() {
        return exits;
    }

    /** Exits from the room */
    final private Map<String, Room> exits;

    public Map<String, Item> getItems() {
        return items;
    }

    /** Items in the room */
    final private Map<String, Item> items;
    /** Characters in the room */
    final private Map<String, Character> characters;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String name, String description)
    {
        this.name = name;
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
        characters = new HashMap<>();
    }

    /**
     * Define 4 exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void setExits(Room north, Room east, Room south, Room west) 
    {
        try {
	    setExit("north", north);
	    setExit("east", east);
	    setExit("south", south);
	    setExit("west", west);
	} catch (BadExitException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    /**
     * Add an exit. Check that neither the direction nor the room is null
     * @param direction Direction to go
     * @param room the adjoining room
     * @throws zuul.BadExitException if the direction is null
     */
    public void setExit(String direction, Room room) throws BadExitException {
    	if (room == null)
    	    return;
        if (direction == null)
            throw new BadExitException(direction, room);
        exits.put(direction, room);
    }

    public void removeExit(String dir) {
        if (exits.containsKey(dir)) {
            exits.remove(dir);
        }
    }
    
    /**
     * Get the room from an exit direction
     * @param direction the direction
     * @return the room to go to
     */
    public Room getExit(String direction) { return exits.get(direction); }
    
    /**
     * @return The description of the room.
     */
    public String getDescription() { return description; }

    /**
     * get all details
     * @return list with all the details
     */
    public List<String> getDetails() {
    	List<String> rv = new LinkedList<>();
        rv.add(Game.messages.getString("in") + " " + getDescription()); //You are in
        
        String tmp = Game.messages.getString("exits") + ": ";
        tmp = exits.keySet().stream()
                .map((dir) -> dir + ' ')
                .reduce(tmp, String::concat);
        rv.add(tmp);
        
        tmp = Game.messages.getString("items") + ": ";
        tmp = items.keySet().stream()
                .map((desc) -> desc + '(' + items.get(desc).getWeight() + ')')
                .reduce(tmp, String::concat);
        rv.add(tmp);
        
        tmp = Game.messages.getString("characters") + ": ";
        tmp = characters.keySet().stream()
                .map((desc) -> desc + ' ')
                .reduce(tmp, String::concat);    
        rv.add(tmp);
        
        return rv;
    }

    /**
     * get item details
     * @return string with details
     */
    public String getTtemDetails() {
        String tmp = "";
        tmp = items.keySet().stream()
                .map((desc) -> desc + '(' + items.get(desc).getWeight() + ')')
                .reduce(tmp, String::concat);
        return tmp;
    }

    public String getCharacterDetails() {
        String tmp = "";
        tmp = characters.keySet().stream()
                .map((desc) -> desc + ' ')
                .reduce(tmp, String::concat);
        return tmp;
    }
    
    /**
     * Add an item to the Room
     * @param description The description of the item
     * @param weight The item's weight
     */
    public void addItem(String description, int weight) {
        addItem(description, new Item(description, weight));               
    }

    
    public void addItem(String desc, Item item) { items.put(desc, item); }
    
    /**
     * Does the room contain an item
     * @param description the item
     * @return the item's weight or 0 if none
     */
    public boolean containsItem(String description) { return items.containsKey(description); }

    public boolean containsItem() { return (items.size() > 0);}
    /*
     * Get an item from the room if it is there
     */
    public Item getItem(String description) { return items.get(description); }
    
    /**
     * Remove an item from the Room
     * @param description the name of the item to remove
     * @return the Item removed or null if no item of that name 
     */
    public Item removeItem(String description) {
        if (items.containsKey(description)) {
            return items.remove(description);
        }
        else {
            return null;
        }
    }

    /**
     * Get a character in the room
     * @param name the name of the Character
     * @return the character or null if no character of that name
     */
    public Character getCharacter(String name) { return characters.get(name); }

    /**
     * Add a character to this room
     * @param character the character to set
     */
    public void addCharacter(Character character) { characters.put(character.getName(),character); }
    
    /** @return A set of all the exits from this room */
    public Set<String> getAllExits() { return exits.keySet(); }

    /**
     * Remove a character from this room
     * @param name the name of the character to remove
     */
    public void removeCharacter(String name) { characters.remove(name); }

    /**
     * Is a character in the room?
     * @param whom the name of the character
     * @return true iff this character is in the room
     */
    public boolean hasCharacter(String whom) { return characters.containsKey(whom); }

    /**
     * get name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get characters
     * @return characters
     */
    public Map<String, Character> getCharacters() {
        return characters;
    }
}
