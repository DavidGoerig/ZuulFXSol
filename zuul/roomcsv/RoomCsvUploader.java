package zuul.roomcsv;

import javafx.util.Pair;
import zuul.Room;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomCsvUploader {

    private Supplier<Stream<String>> loadCsvIntoStream(Path pathCsvFile) throws IOException {
       // reading csv file into stream, try-with-resources
        List<String> allLines = Files.readAllLines(pathCsvFile);
        return allLines::stream;
    }

    private HashMap<String, Room> createRooms(Supplier<Stream<String>> supplier) {
        List<Pair> list = new ArrayList<>();
        HashMap<String, Room> roomList = new HashMap<>();
        list = supplier.get().map(line -> {
            String[] str = line.split(",");
            Pair pair = new Pair(str[0], str[1]);
            return pair;
        }).collect(Collectors.toList());
        list.stream()
                .forEach(item->roomList.put(item.getKey().toString(),
                        new Room(item.getKey().toString(), item.getValue().toString())));
        return roomList;
    }

    public HashMap<String, Room> createRoomsFromCsv(File csvFile) throws IOException {
        Path pathCsvFile = Paths.get("zuul/res/config_file/game1.csv");
        HashMap<String, Room> allRooms;
        Supplier<Stream<String>> supplierStreamCsv = loadCsvIntoStream(pathCsvFile);
        allRooms = this.createRooms(supplierStreamCsv);
        this.initialiseExits(supplierStreamCsv, allRooms);
        this.addObjects(supplierStreamCsv, allRooms);
        return allRooms;
    }

    private void addObjects(Supplier<Stream<String>> supplier, HashMap<String, Room> allRooms) {
        HashMap<String, List<Pair>> objects = new HashMap<>();
        List<String[]> list = new ArrayList<>();
        list = supplier.get()
                .filter(line -> line.split(",").length > 6)
                .map(line -> {
            String[] str = line.split(",");
            String[] obj = Arrays.copyOfRange(str, 6, str.length);
            String[] fin = new String[obj.length + 1];
            fin[0] = str[0];
            System.arraycopy(obj, 0, fin, 1, obj.length);
            return fin;
        }).collect(Collectors.toList());
        list.forEach(item-> {
            for (int i = 0; i < (item.length - 1) / 2; i++) {
                String objname = item[i * 2 + 1].replaceAll(" ","");
                Integer weight = Integer.valueOf(item[i * 2 + 2].replaceAll(" ",""));
                allRooms.get(item[0]).addItem(objname, weight);
            }
        });
    }

    private void initialiseExits(Supplier<Stream<String>> supplier, HashMap<String, Room> allRooms) {
        List<String[]> list = new ArrayList<>();
        list = supplier.get().map(line -> {
            String[] str = line.split(",");
            return new String[]{str[0], str[2], str[3], str[4], str[5]};
        }).collect(Collectors.toList());
        list.forEach(item-> {
            allRooms.get(item[0]).setExits(
                    allRooms.get(item[1].replaceAll(" ","")),
                    allRooms.get(item[2].replaceAll(" ","")),
                    allRooms.get(item[3].replaceAll(" ","")),
                    allRooms.get(item[4].replaceAll(" ",""))
            );});
    }
}
