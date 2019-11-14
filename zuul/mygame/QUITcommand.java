
package zuul.mygame;

import zuul.Game;
import zuul.Player;
import zuul.command.Command;

/**
 * Command to quit
 * @author rej
 */
public class QUITcommand extends Command {
    
    public QUITcommand(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public QUITcommand() {}
    
    @Override
    public boolean execute(Player player) {       
        if (hasSecondWord()) {
            Game.out.println(Game.messages.getString("quitWhat"));
            return false;
        }
        return true;  // if we get here, signal that we want to quit
    }
}
