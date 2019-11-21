package zuul.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class HighScore {
    private String path = "HighScore.txt";
    private ArrayList<String[]> array = new ArrayList<String[]>();

    public HighScore() {
    }

    public void readHighScoreFromFile() {
        File file = new File(path);
        int id = 0;

        try {
            if (!file.createNewFile()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String next = sc.nextLine();
                    if (id % 2 == 0) {
                        String arr[] = new String[2];
                        arr[0] = next;
                        array.add(arr);
                    } else {
                        array.get(id / 2)[1] = next;
                    }
                    id++;
                }
                sc.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortArray() {
        Collections.sort(array, new Comparator<String[]>() {
            @Override
            public int compare(String[] pos1, String[] pos2) {
                int score1 = Integer.parseInt(pos1[1]);
                int score2 = Integer.parseInt(pos2[1]);
                return score1 > score2 ? -1 : score1 < score2 ? 1 : 0;
            }
        });
    }

    public void addScore(String userName, String points) {
        readHighScoreFromFile();
        String newScore[] = { userName, points };
        array.add(newScore);
        try {
            writeNewHighScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNewHighScore() throws IOException {
        File file = new File(path).getAbsoluteFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        getAllScores().forEach(el -> {
            try {
                writer.write(el[0] + "\n" + el[1] + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();
    }

    public ArrayList<String[]> getAllScores() {
        sortArray();
        return array;
    }
}
