package zuul;

import java.io.File;
import java.io.IOException;
import java.util.*;

import zuul.room.Room;
import zuul.roomcsv.RoomCsvUploader;

/**
 * This class is the main class of the "World of Zuul" application. "World of
 * Zuul" is a very simple, text based adventure game. Users can walk around some
 * scenery. That's all. It should really be extended to make it more
 * interesting!
 *
 * To play this game, create an instance of this class and call the "play"
 * method.
 *
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates and executes the
 * commands that the parser returns.
 *
 * @author Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
public abstract class Game {
    /**
     * Room file checker and file uploader
     */
    //public static final RoomCsvChecker roomCsvChecker;

    public static final RoomCsvUploader roomCsvUploader = new RoomCsvUploader();

    /**
     * Internationalisation messages
     */
    public static ResourceBundle messages;

    private Player player;

    /**
     * get for rooms
     * @return all rooms
     */
    public HashMap<String, Room> getAllRooms() {
        return allRooms;
    }

    protected HashMap<String, Room> allRooms;

    /**
     * Create the game and initialise its internal map.
     *
     * @param language The language to use
     * @param country The country
     * @see java.util.Locale
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Game(String language, String country) throws IOException {
        Locale currentLocale = new Locale(language, country);
        messages = ResourceBundle.getBundle("zuul.mygame.MessagesBundle", currentLocale);
    }

    /**
     * Set bundle to chosen one using language and country
     * @param language chosen language for bundle
     * @param country chosen country for bundle
     */
    public void setBundle(String language, String country) {
        Locale currentLocale = new Locale(language, country);
        messages = ResourceBundle.getBundle("zuul.mygame.MessagesBundle", currentLocale);

    }

    /**
     * Set the player
     * @param p the player
     */
    protected void setPlayer(Player p) {
        player = p;
    }

    /**
     * Get the player
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the complete list of rooms.
     * The game needs to know all the rooms so that it can iterate through them
     * @param all The list of rooms
     */
    public void setAllRooms(HashMap<String, Room> all) {
        allRooms = all;
    }

    /*------------------ Game specific stuff ----------------------------*/
    /**
     * Create all the rooms and link their exits together.
     */
    protected abstract void createRooms(File file) throws IOException;


    /**
     * method to create player
     * @param playerName player name
     */
    public abstract void createPlayer(String playerName);

    /**
     * method for create character
     * @param name character name
     * @param r chosen room
     */
    public abstract void createCharacter(String name, Room r);

    /**
     * method for delete a room
     * @param r chosen room
     */
    public abstract void deleteRoom(Room r);
}
