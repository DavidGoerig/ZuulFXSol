package zuul.mygame;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class holds an enumeration of all command words known to this particular game.
 *
 * @author  Michael Kolling and David J. Barnes
 */

public class CommandWords extends zuul.command.CommandWords {
    // a constant array that holds all valid command words
    private static final String[] validCommands = {
        "go", "quit", "help", "look", "take", "drop", "give"
    };

    public CommandWords() {}
    
    /** @return the validCommands */
    @Override
    public  String[] getValidCommands() { return validCommands; }

}
