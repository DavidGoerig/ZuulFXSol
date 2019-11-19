package zuul.roomcsv;

import javafx.util.Pair;
import zuul.Room;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomCsvChecker {
    /*
    String fileName = "c://lines.txt";
		List<String> list = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			//1. filter line 3
			//2. convert all content to upper case
			//3. convert it into a List
			list = stream
					.filter(line -> !line.startsWith("line3"))
					.map(String::toUpperCase)
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		list.forEach(System.out::println);

		DANS LE FILTER METTRE UNE FUNCTION QUI RETOURNE TRUE SI:
		Il y a bien le nom, 4 connexions, etc. sert juste a check string / int, si les objets sont bien Pair<int, string>

		list = stream.filter(line -> line.contains("BA-731") || line.contains("VA-421")).map(line -> {

		Avec ca on peut check les doublons

		Sert à tout sauf check la parité des trucs
     */
    private String errorMessage = null;

    private Supplier<Stream<String>> loadCsvIntoStream(Path pathCsvFile) throws IOException {
        // reading csv file into stream, try-with-resources
        List<String> allLines = Files.readAllLines(pathCsvFile);
        return allLines::stream;
    }

    public boolean checkFile() throws IOException {
        Path pathCsvFile = Paths.get("zuul/res/config_file/game1.csv");
        Supplier<Stream<String>> supplierStreamCsv = loadCsvIntoStream(pathCsvFile);
        return isFileWellFormed(supplierStreamCsv);
    }



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

    private boolean weighPositiveInteger(Supplier<Stream<String>> supplier) {
        List<Pair> allItems = this.getItems(supplier);
        List<Pair> list = allItems.stream()
                .filter(line -> !isPositiveInt(line.getValue().toString()))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            setErrorMessage("One of weight is not a positive integer:" + list);
            return false;
        }
        return true;
    }

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

    private boolean itemDifferentName(Supplier<Stream<String>> supplier) {
        List<Pair> allItems = this.getItems(supplier);
        List<String> allItemName = allItems.stream()
                .map(l -> l.getKey().toString())
                .collect(Collectors.toList());
        List<String> dupplication = allItemName.stream()
                .filter(l -> Collections.frequency(allItemName, l) != 1)
                .collect(Collectors.toList());
        if (dupplication.size() > 0) {
            setErrorMessage("ERROR: this item are duplicated:" + dupplication);
            return false;
        }
        return true;
    }

    private boolean exitExistingRoom(Supplier<Stream<String>> supplier) {
        List<String> allRoomName = this.getRoomNames(supplier);
        List<String> allExitName = this.getExitName(supplier);
        allRoomName.add("null");
        List<String> list = allExitName.stream()
                .filter(line -> allRoomName.contains(line))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            setErrorMessage("One of the exit is not an existing room.:" + list);
            return false;
        }
        return true;
    }

    private List<String> getRoomNames(Supplier<Stream<String>> supplier) {
        return supplier.get().map(line -> {
            String[] str = line.split(",");
            return str[0];
        }).collect(Collectors.toList());
    }

    private List<String> getExitName(Supplier<Stream<String>> supplier) {
        List<String[]> list = supplier.get().map(line -> {
            String[] str = line.split(",");
            return new String[]{str[2], str[3], str[4], str[5]};
        }).collect(Collectors.toList());
        List<String> allRooms = new ArrayList<>();
        list.forEach(l -> Collections.addAll(allRooms, l));
        return allRooms;
    }

    private boolean objectPair(Supplier<Stream<String>> supplier) {
        List<String> list = supplier.get()
                .filter(line->line.split(",").length % 2 != 0)
                .collect(Collectors.toList());
        if (list.size() > 0) {
            // TODO le message d'erreur en faire qqch de mieux
            setErrorMessage("Objects need to be in this format: name weight:" + list);
            return false;
        }
        return true;
    }

    private boolean allRoomInformation(Supplier<Stream<String>> supplier) {
        List<String> list = new ArrayList<>();
        list = supplier.get()
                .filter(line->line.split(",").length < 6)
                .collect(Collectors.toList());
        if (list.size() > 0) {
            // TODO le message d'erreur en faire qqch de mieux
            setErrorMessage("Error, not all information for every rooms.:" + list);
            return false;
        }
        return true;
    }

    private boolean identifiersPairwise(Supplier<Stream<String>> supplier) {
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
