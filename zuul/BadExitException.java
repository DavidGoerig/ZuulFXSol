package zuul;

public class BadExitException extends Exception {

    private static final long serialVersionUID = -6960984107626797656L;

    /**
     * Exception constructor for use when a bad exit is requested
     * @param direction The direction to exit
     * @param r The next room 
     */
    public BadExitException(String direction, Room r) {
        super(direction == null && r == null
                ? "Direction and room are null"
                : direction == null
                        ? "Direction is null"
                        : r == null
                                ? "Room is null"
                                : "Something bad about an exit");
    }
}
