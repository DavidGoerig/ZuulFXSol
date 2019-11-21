package zuul.mygame;

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
    protected void createRooms() throws IOException {
        //Room outside, theatre, pub, lab, office;
        RoomCsvChecker check = new RoomCsvChecker();
        if (!check.checkFile()) {
            System.out.println(check.getErrorMessage());
            System.exit(0);
        }
        allRooms = roomCsvUploader.createRoomsFromCsv(inputFile);

        RoomModifyier mod = new RoomModifyier();
        allRooms = mod.newItemAllRoomWithoutExits(allRooms, "oui", 2);
        allRooms = mod.removeAllWithoutExits(allRooms);
        allRooms = mod.removeAllWithoutItems(allRooms);

        setAllRooms(allRooms);
        /*
        allRooms = new ArrayList<>();

        // create the rooms
        outside = new Room(messages.getString("outside")); // outside the main entrance of the university
        allRooms.add(outside);
        theatre = new Room(messages.getString("lecture")); // in a lecture theatre
        allRooms.add(theatre);
        pub = new Room(messages.getString("pub")); // in the campus pub
        allRooms.add(pub);
        lab = new Room(messages.getString("lab")); // in a computing lab
        allRooms.add(lab);
        office = new Room(messages.getString("admin")); // in the computing admin office
        allRooms.add(office);
       
        // initialise room exits
        outside.setExits(null, theatre, lab, pub);
        outside.addItem(messages.getString("notebook"), 2);
        theatre.setExits(null, null, null, outside);
        pub.setExits(null, outside, null, null);
        lab.setExits(outside, office, null, null);
        office.setExits(null, null, null, lab);

        setAllRooms(allRooms);

         */
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
    protected void createPlayer(String playerName) {
        // le foutre dans une pièce random
        //TODO ENLEVER CETTE BOUSE DE FONCTION RANDOM ISSOU

        Map.Entry<String, Room> entry = allRooms.entrySet().iterator().next();
        Room randomRoom = entry.getValue();
        //Random r = new Random();
        //Room randomRoom = allRooms.stream().skip(r.nextInt(allRooms.size()-1)).findFirst().get();
        setPlayer(new Player(messages.getString(playerName), randomRoom));  // start game outside
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
