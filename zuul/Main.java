package zuul;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.io.IOException;

/**
 *
 * @author rej
 */
public class Main {

    /**
     * The main entry point
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Pair<String, String> langCountry = new Pair<>("en", "us");
        new zuul.mygame.MyGame(langCountry.getKey(), langCountry.getValue()).play();
    }
}
