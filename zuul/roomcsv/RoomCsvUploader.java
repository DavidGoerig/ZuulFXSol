package zuul.roomcsv;

import javafx.util.Pair;
import zuul.Room;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomCsvUploader {

    private Supplier<Stream<String>> loadCsvIntoStream(Path pathCsvFile) throws IOException {
       // reading csv file into stream, try-with-resources
        List<String> allLines = Files.readAllLines(pathCsvFile);
        return allLines::stream;
    }

    private ArrayList<Room> createRooms(Supplier<Stream<String>> supplier) {
        List<Pair> list = new ArrayList<>();
        ArrayList<Room> roomList = new ArrayList<>();
        list = supplier.get().map(line -> {
            String[] str = line.split(",");
            Pair pair = new Pair(str[0], str[1]);
            return pair;
        }).collect(Collectors.toList());
        list.stream()
                .forEach(item->roomList.add(new Room(item.getKey().toString(), item.getValue().toString())));
        return roomList;
    }

    public List<Room> createRoomsFromCsv(File csvFile) throws IOException {
        Path pathCsvFile = Paths.get("zuul/res/config_file/game1.csv");
        List allRooms = new ArrayList<>();
        Supplier<Stream<String>> supplierStreamCsv = loadCsvIntoStream(pathCsvFile);
        allRooms = this.createRooms(supplierStreamCsv);
        //allRooms = this.initialiseExits(doc, allRooms);
        //allRooms = this.addObjects(doc, allRooms);
        return allRooms;
    }
}
