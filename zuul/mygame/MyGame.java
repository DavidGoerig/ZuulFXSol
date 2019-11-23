package zuul.mygame;

import java.io.File;
import java.io.IOException;
import java.util.*;

import zuul.Game;
import zuul.Item;
import zuul.Player;
import zuul.room.Room;
import zuul.room.RoomModifyier;
import zuul.roomcsv.RoomCsvChecker;

public class MyGame extends Game {

    public MyGame(String language, String country) throws IOException {
	super(language, country, new zuul.mygame.CommandWords());
    }
	
    /** Create all the rooms and link their exits together.  */
    @Override
    public void createRooms(File csvFile) throws IOException {
        allRooms = roomCsvUploader.createRoomsFromCsv(csvFile);
    }

    @Override
    protected List<String> getWelcomeStrings() {
    	List<String> rv = new LinkedList<>();
    	rv.add("");
    	rv.add(messages.getString("welcome")); // Welcome to the World of Zuul!
    	rv.add(messages.getString("zuul")); // World of Zuul is a new, incredibly boring adventure game.
    	rv.add(messages.getString("getHelp")); // Type 'help' if you need help.
    	rv.add("");
    	rv.addAll(getPlayer().getDetails());
    	return rv;
    }

    @Override
    public void createPlayer(String playerName) {
        Map.Entry<String, Room> entry = allRooms.entrySet().iterator().next();
        Room randomRoom = entry.getValue();
        setPlayer(new Player(playerName, randomRoom));
    }

    @Override
    protected void createCharacter() {
        // add a character
        // here I create a new anonymous class that is a subclass of Character with a
        // different execute method
        /*lab.addCharacter(new Character(messages.getString("Cecilia"), lab) {
            @Override
            public void execute() {
                randomMove();
            }
        });

         */
    }
}
