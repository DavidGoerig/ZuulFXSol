package zuul.model;

public class GameBoard {
    private int width;
    private int height;

    /**
     * Game board class
     * @param x x size
     * @param y y size
     */
    public GameBoard(int x, int y) {
        width = x;
        height = y;
    }

    /**
     *  get width
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * get height
     * @return height
     */
    public int getHeight() {
        return height;
    }
}
