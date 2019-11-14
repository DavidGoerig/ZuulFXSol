
package zuul.mygame;

import zuul.Game;
import zuul.Player;
import zuul.command.Command;

/**
 * Command to give something to someone
 * @author rej
 */
public class GIVEcommand extends Command {
    
    public GIVEcommand(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public GIVEcommand() {}
    
    @Override
    public boolean execute(Player player) {
        if (!hasSecondWord()) {
            // if there is no second word, we don't know what to give...
            Game.out.println(Game.messages.getString("giveWhat"));
            return false;
        }
        if (!hasThirdWord()) {
            // if there is no third word, we don't to whom to give it...
            Game.out.println(Game.messages.getString("giveWho"));
            return false;
        }
        String desc = getSecondWord();
        String whom = getThirdWord();
        player.give(desc, whom);
        return false;
    }
}
