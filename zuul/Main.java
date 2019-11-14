package zuul;

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
    	String language;
    	String country;
    	// TODO improve error handling
    	if (args.length != 2) {
            language = "en";
            country = "US";
        } else {
            language = args[0];
            country = args[1];
        }
    	
    	// Start specific game 
        new zuul.mygame.MyGame(language, country).play();
    }
}
