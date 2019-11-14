package zuul;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import zuul.command.Command;
import zuul.command.CommandWords;
import zuul.command.Parser;
import zuul.io.In;
import zuul.io.Out;

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
     * Delegate all output messages to out
     */
    public static final Out out = new Out();
    /**
     * Delegate all input messages to in
     */
    public static final In in = new In();

    /**
     * Internationalisation messages
     */
    public static ResourceBundle messages;
    
    /**
     * The commands known to the game
     */
    public static CommandWords commands;
    private final Parser parser;

    private Player player;
    protected List<Room> allRooms;

    /**
     * Create the game and initialise its internal map.
     *
     * @param language The language to use
     * @param country The country
     * @param commands The available command words
     * @see java.util.Locale
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Game(String language, String country, CommandWords commands) {
        // internationalise
        Locale currentLocale = new Locale(language, country);
        messages = ResourceBundle.getBundle("zuul.mygame.MessagesBundle", currentLocale);
        Game.commands = commands;
        parser = new Parser("zuul.mygame");
        createRooms();
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            allRooms.stream().forEach((r) -> {
                r.process();
            });
            finished = processCommand(command);
        }
        Game.out.println(messages.getString("goodbye")); //Thank you for playing.  Good bye.
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        getWelcomeStrings().stream().forEach((str) -> {
            Game.out.println(str);
        });
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        return command.execute(player);
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
    protected Player getPlayer() {
        return player;
    }

    /**
     * Set the complete list of rooms.
     * The game needs to know all the rooms so that it can iterate through them
     * @param all The list of rooms
     */
    protected void setAllRooms(List<Room> all) {
        allRooms = all;
    }

    /*------------------ Game specific stuff ----------------------------*/
    /**
     * Create all the rooms and link their exits together.
     */
    protected abstract void createRooms();

    /*
     * All the welcome messages
     */
    protected abstract List<String> getWelcomeStrings();

}
