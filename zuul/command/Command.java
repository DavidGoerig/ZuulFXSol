package zuul.command;

import zuul.Player;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. 
 *
 * If the command had only one word, then the second word is null.
 * 
 * @author  Michael Kolling and David J. Barnes
 */

public abstract class Command
{
    private String commandWord;
    private String secondWord;
    private String thirdWord;

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     * @param firstWord The first word of the command. 
     * @param secondWord The second word of the command.
     * @param thirdWord The second word of the command.
     */
    public Command(String firstWord, String secondWord, String thirdWord)
    {
        commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = thirdWord;
    }
    
    public Command () {}
    
    /**
     * Add arguments to the command
     * @param firstWord
     * @param secondWord
     * @param thirdWord
     */
    public void addWords(String firstWord, String secondWord, String thirdWord) {
        commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = thirdWord;

    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord() { return commandWord; }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord() { return secondWord; }

    /**
     * @return The third word of this command. Returns null if there was no
     * third word.
     */
    public String getThirdWord() { return thirdWord; }
    
    /**
     * @return true if this command was not understood.
     *
    public boolean isUnknown() { return (commandWord == null); }
    */

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() { return (secondWord != null); }
    
    /**
     * @return true if the command has a third word.
     */
    public boolean hasThirdWord() { return (thirdWord != null); }
    
    public abstract boolean execute(Player player);
}

