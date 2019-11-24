package zuul.model;

public class GameBoard {
    private int width;
    private int height;

    public GameBoard(int x, int y) {
        width = x;
        height = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
