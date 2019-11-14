package zuul.command;

import java.util.MissingResourceException;
import java.util.Scanner;

import zuul.Game;
import zuul.mygame.UnknownCommand;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a three word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
public class Parser 
{
    private final CommandWords commands;  // holds all valid command words
    private final Scanner reader;         // source of command input
    private final String MYPACKAGE; // name of the package with the game-specific classes

    /**
     * Create a parser to read from the terminal window.
     * @param pkg the package of command classes
     */
    public Parser(String pkg) {
        this.commands = Game.commands;
        reader = new Scanner(Game.in.in);
        MYPACKAGE = pkg + '.';
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;
        String word3 = null;

        Game.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        try ( // Find up to two words on the line.
            // Note this construct will auto close the input 
            Scanner tokenizer = new Scanner(inputLine)) {
                if(tokenizer.hasNext()) {
                    word1 = tokenizer.next();      // get first word
                    if(tokenizer.hasNext()) {
                        word2 = tokenizer.next();      // get second word
                    }
                    if(tokenizer.hasNext()) {
                        word3 = tokenizer.next();      // get second word
                        // note: we just ignore the rest of the input line.
                    }
                }
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        try {
            word1 = Game.messages.getString(word1); // translate it
        }
        catch (MissingResourceException | NullPointerException e) {
            return new UnknownCommand(word1, word2, word3);
        }
        if(commands.isCommand(word1)) {
            String cmdString = MYPACKAGE + word1.toUpperCase() + "command";
            try {
                Command cmd = (Command) Class.forName(cmdString).newInstance();
                cmd.addWords(word1, word2, word3);
                //Could use the Constructor class but this is easier
                return cmd;
           }
            catch (ClassNotFoundException  
                   | InstantiationException 
                   | IllegalAccessException 
                   | SecurityException  
                   | IllegalArgumentException 
                   e ) 
            { 
                return new UnknownCommand(word1, word2, word3);
            } 
        }
        else {
            return new UnknownCommand(word1, word2, word3); 
        }
    }
}
