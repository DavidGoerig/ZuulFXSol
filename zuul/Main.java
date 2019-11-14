package zuul;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Pair;
/**
 *
 * @author rej
 */
public class Main {

    /**
     * The main entry point
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Pair<String, String> langCountry = new Pair<>("en", "us");
        new zuul.mygame.MyGame(langCountry.getKey(), langCountry.getValue()).play();
    }
}
