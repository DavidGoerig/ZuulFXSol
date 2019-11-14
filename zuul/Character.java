package zuul;

import java.util.Random;

/**
 * A simple class for Characters
 * @author rej
 */
public abstract class Character {
    private final String name;
    private Room currentRoom;
    
    /**
     * Place a new character in a room
     * @param n the name of the character
     * @param r the room in which to place them
     */
    public Character(String n, Room r) {
        name = n;
        currentRoom = r;
    }

    /**
     * @return the name
     */
    public String getName() { return name; }

    /**
     * @return the room the character is in
     */
    public Room getCurrentRoom() { return currentRoom; }

    /**
     * Place the character in a room
     * @param currentRoom the currentRoom to set
     */
    public void setCurrentRoom(Room currentRoom) { this.currentRoom = currentRoom; }
 
    /**
     * Move the character to a randomly chosen adjacent room
     */
    public void randomMove() {
        String[] exits =  currentRoom.getAllExits().toArray(new String[0]); //parameter to force type
        int numExits = exits.length;
        if (numExits == 0)
            return; // strange room but be safe!	
        int n = new Random().nextInt(numExits);
        Room nextRoom = currentRoom.getExit(exits[n]);
        currentRoom.removeCharacter(name);
        nextRoom.addCharacter(this);
        //Game.out.println(name + " moves from " + currentRoom.getDescription() + " to " + nextRoom.getDescription());
        currentRoom = nextRoom;
    }
    
    
    /**
     * Game-specific action 
     */
    public void execute() {}
}
