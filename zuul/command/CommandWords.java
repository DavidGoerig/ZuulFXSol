package zuul.command;

import zuul.Game;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */

public abstract class CommandWords 
{
    /**
     * Constructor - initialise the command words.
     */
    public CommandWords() {
        // nothing to do at the moment...
    }

    /**
     * @return the validCommands
     */
    public abstract String[] getValidCommands();
    
    /**
     * Check whether a given String is a valid command word. 
     * @param aString the possible command 
     * @return true if a given string is a valid command,
     * false if it isn't.
     */
    public boolean isCommand(String aString)  {
        for(String cmd : getValidCommands()) {
            if (cmd.equals(Game.messages.getString(aString)))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }
}
