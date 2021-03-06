package zuul.roomcsv;

import javafx.util.Pair;
import zuul.Game;
import zuul.room.Room;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomCsvChecker {
    private String errorMessage = null;

    /**
     *  load csv into stream
     * @param pathCsvFile csv path
     * @return stream supplier
     * @throws IOException
     */
    private Supplier<Stream<String>> loadCsvIntoStream(Path pathCsvFile) throws IOException {
        // reading csv file into stream, try-with-resources
        List<String> allLines = Files.readAllLines(pathCsvFile);
        return allLines::stream;
    }

    /**
     * main entry to check the file
     * @param pathCsvFile csv path string
     * @return is file correct or not
     * @throws IOException
     */
    public boolean checkFile(Path pathCsvFile) throws IOException {
        Supplier<Stream<String>> supplierStreamCsv = loadCsvIntoStream(pathCsvFile);
        return isFileWellFormed(supplierStreamCsv);
    }

    /**
     *  check if the file follow our pattern
     * @param supplier stream supplier containing the file
     * @return is file correct or not
     */
    private boolean isFileWellFormed(Supplier<Stream<String>> supplier) {
        if (!this.allRoomInformation(supplier)) {
            return false;
        }
        if (!this.objectPair(supplier)) {
            return false;
        }
        if (!this.identifiersPairwise(supplier)) {
            return false;
        }
        if (!this.exitExistingRoom(supplier)) {
            return false;
        }
        if (!this.itemDifferentName(supplier)) {
            return false;
        }
        if (!this.weighPositiveInteger(supplier)) {
            return false;
        }
        return true;
    }

    /**
     * check if integer is positiv or not
     * @param strNum string with number
     * @return bool
     */
    private static boolean isPositiveInt(String strNum) {
        try {
            int d = Integer.parseInt(strNum);
            if (d <= 0)
                return false;
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    /**
     * check if all weight are positive integer
     * @param supplier supplier
     * @return bool
     */
    private boolean weighPositiveInteger(Supplier<Stream<String>> supplier) {
        List<Pair> allItems = this.getItems(supplier);
        List<Pair> list = allItems.stream()
                .filter(line -> !isPositiveInt(line.getValue().toString()))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            setErrorMessage(Game.messages.getString("posint") + list);
            return false;
        }
        return true;
    }

    /**
     * get all items in the file
     * @param supplier supplier
     * @return all the items as pair
     */
    private List<Pair> getItems(Supplier<Stream<String>> supplier) {
        List<String[]> list = supplier.get()
                .filter(line -> line.split(",").length > 6)
                .map(line -> {
                    String[] str = line.split(",");
                    String[] obj = Arrays.copyOfRange(str, 6, str.length);
                    String[] fin = new String[obj.length + 1];
                    fin[0] = str[0];
                    System.arraycopy(obj, 0, fin, 1, obj.length);
                    return fin;
                }).collect(Collectors.toList());
        List<Pair> objlist = new ArrayList<>();
        list.forEach(item-> {
                    for (int i = 0; i < (item.length - 1) / 2; i++) {
                        String objname = item[i * 2 + 1].replaceAll(" ","");
                        Integer weight = Integer.valueOf(item[i * 2 + 2].replaceAll(" ",""));
                        objlist.add(new Pair(objname, weight));
                    }
        });
        return objlist;
    }

    /**
     * check if all item got different name
     * @param supplier supp
     * @return bool
     */
    private boolean itemDifferentName(Supplier<Stream<String>> supplier) {
        List<Pair> allItems = this.getItems(supplier);
        List<String> allItemName = allItems.stream()
                .map(l -> l.getKey().toString())
                .collect(Collectors.toList());
        List<String> dupplication = allItemName.stream()
                .filter(l -> Collections.frequency(allItemName, l) != 1)
                .collect(Collectors.toList());
        if (dupplication.size() > 0) {
            setErrorMessage(Game.messages.getString("duplicated") + dupplication);
            return false;
        }
        return true;
    }

    /**
     * check if all exit got existing room
     * @param supplier supplier
     * @return bool
     */
    private boolean exitExistingRoom(Supplier<Stream<String>> supplier) {
        List<String> allRoomName = this.getRoomNames(supplier);
        List<String> allExitName = this.getExitName(supplier);
        allRoomName.add("null");
        List<String> list = allExitName.stream()
                .filter(line -> !allRoomName.contains(line))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            setErrorMessage(Game.messages.getString("exitdontexist") + list);
            return false;
        }
        return true;
    }

    /**
     * get all room names
     * @param supplier supp
     * @return list with all names
     */
    private List<String> getRoomNames(Supplier<Stream<String>> supplier) {
        return supplier.get().map(line -> {
            String[] str = line.split(",");
            return str[0];
        }).collect(Collectors.toList());
    }

    /**
     * get all exit name
     * @param supplier supp
     * @return all exit names
     */
    private List<String> getExitName(Supplier<Stream<String>> supplier) {
        List<String[]> list = supplier.get().map(line -> {
            String[] str = line.split(",");
            return new String[]{
                    str[2].replaceAll(" ",""),
                    str[3].replaceAll(" ",""),
                    str[4].replaceAll(" ",""),
                    str[5].replaceAll(" ","")
            };
        }).collect(Collectors.toList());
        List<String> allRooms = new ArrayList<>();
        list.forEach(l -> Collections.addAll(allRooms, l));
        return allRooms;
    }

    /**
     * check if obkect are pair (one name one weight)
     * @param supplier supplier
     * @return bool
     */
    private boolean objectPair(Supplier<Stream<String>> supplier) {
        List<String> list = supplier.get()
                .filter(line->line.split(",").length % 2 != 0)
                .collect(Collectors.toList());
        if (list.size() > 0) {
            setErrorMessage(Game.messages.getString("objformat") + list);
            return false;
        }
        return true;
    }

    /**
     * find all romm information in supplier
     * @param supplier supp
     * @return all room info
     */
    private boolean allRoomInformation(Supplier<Stream<String>> supplier) {
        List<String> list = new ArrayList<>();
        list = supplier.get()
                .filter(line->line.split(",").length < 6)
                .collect(Collectors.toList());
        if (list.size() > 0) {
            setErrorMessage(Game.messages.getString("info") + list);
            return false;
        }
        return true;
    }

    /**
     * check if identifiers are pair wised (north with south, east with west)
     * @param supplier supp
     * @return bool
     */
    private boolean identifiersPairwise(Supplier<Stream<String>> supplier) {
        HashMap<String, Room> roomList = new HashMap<>();
        String[] directions = {"north", "east", "south", "west"};
        List<Pair> list = supplier.get().map(line -> {
            String[] str = line.split(",");
            return new Pair(str[0], str[1]);
        }).collect(Collectors.toList());
        list.stream()
                .forEach(item->roomList.put(item.getKey().toString(),
                        new Room(item.getKey().toString(), item.getValue().toString())));
        RoomCsvUploader uploader = new RoomCsvUploader();
        uploader.initialiseExits(supplier, roomList);
        for (Map.Entry<String, Room> stringRoomEntry : roomList.entrySet()) {
            Map.Entry entry = stringRoomEntry;
            for (String dir: directions) {
                Room room = (Room) entry.getValue();
                Room exitRoom = room.getExit(dir);
                if (exitRoom != null && exitRoom.getExit(this.findOppositeDir(dir)) != room) {
                    setErrorMessage(Game.messages.getString("pair") + room.getName() + "->" + dir + " / " + exitRoom.getName() + "->" + this.findOppositeDir(dir));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * return opposite dir than the given one
     * @param dir
     * @return opposite dir
     */
    private String findOppositeDir(String dir) {
        switch (dir) {
            case "north":
                return "south";
            case "south":
                return "north";
            case "east":
                return "west";
            case "west":
                return "east";
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage
     */
    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
