/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zuul.mygame;

import zuul.Game;
import zuul.Player;
import zuul.command.Command;

/**
 * Command to get help
 * @author rej
 */
public class HELPcommand extends Command {
    
    public HELPcommand(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public HELPcommand() {}
    
    @Override
    public boolean execute(Player player) {
    	for (String str : getInstructions()) 
    	    Game.out.println(str);
        return false;
    }
    
    private String[] getInstructions() {
        String[] rv = new String[4];
        rv[0] = Game.messages.getString("lost"); // You are lost. You are alone. You wander around at the university.
        rv[1] = "";
        rv[2] = Game.messages.getString("commands"); // Your command words are:
        String tmp = "   ";
        for (String cmd : Game.commands.getValidCommands())
            tmp += cmd + ' ';
        rv[3] = tmp;
        return rv;
    }
}
