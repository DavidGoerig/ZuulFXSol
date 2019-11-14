/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zuul.mygame;

import zuul.Game;
import zuul.Player;
import zuul.command.Command;

/**
 * Command to drop something
 * @author rej
 */
public class DROPcommand extends Command {
    
    public DROPcommand(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public DROPcommand() {}
    
    @Override
    public boolean execute(Player player) {
        if (!hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            Game.out.println(Game.messages.getString("dropWhat"));
            return false;
        }
        String desc = getSecondWord();
        player.drop(desc);
        return false;
    }
}
