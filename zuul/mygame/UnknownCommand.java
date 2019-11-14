
package zuul.mygame;

import zuul.Game;
import zuul.Player;
import zuul.command.Command;

/**
 * This command is not recognised
 * @author rej
 */
public class UnknownCommand extends Command {
    
    public UnknownCommand(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public UnknownCommand() {}
    
    @Override
    public boolean execute(Player player) {
        // We got bad input
        Game.out.println(Game.messages.getString("unknown")); // I don't know what you mean...
        return false;
    }
}
