package zuul.mygame;

import java.io.File;
import java.io.IOException;
import java.util.*;

import zuul.Game;
import zuul.Player;
import zuul.room.Room;
import zuul.Character;

public class MyGame extends Game {

    public MyGame(String language, String country) throws IOException {
	super(language, country);
    }
	
    /** Create all the rooms and link their exits together.  */
    @Override
    public void createRooms(File csvFile) throws IOException {
        allRooms = roomCsvUploader.createRoomsFromCsv(csvFile);
    }

    /**
     * create players
     * @param playerName player name
     */
    @Override
    public void createPlayer(String playerName) {
        Map.Entry<String, Room> entry = allRooms.entrySet().iterator().next();
        Room randomRoom = entry.getValue();
        setPlayer(new Player(playerName, randomRoom));
    }

    /**
     * create char
     * @param name character name
     * @param r chosen room
     */
    @Override
    public void createCharacter(String name, Room r) {
        // add a character
        // here I create a new anonymous class that is a subclass of Character with a
        // different execute method
        r.addCharacter(new Character(name, r) {
            @Override
            public void execute() {
                randomMove();
            }
        });
    }

    /**
     * delete room
     * @param r chosen room
     */
    @Override
    public void deleteRoom(Room r) {
        Iterator iterator = allRooms.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (r.equals(entry.getValue())) {
                iterator.remove();
            }
        }
    };
}
