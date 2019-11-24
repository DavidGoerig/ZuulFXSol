package zuul.model;

public class GameBoard {
    private int width;
    private int height;

    public GameBoard(int x, int y) {
        width = x;
        height = y;
    }

    public void setGridSize(String width, String height) {
        boolean validGridSize = width.matches("-?\\d+") && height.matches("-?\\d+") && Integer.parseInt(width) >= 10
                && Integer.parseInt(width) <= 100 && Integer.parseInt(height) >= 10 && Integer.parseInt(height) <= 100;
        if (validGridSize) {
            this.width = Integer.parseInt(width);
            this.height = Integer.parseInt(height);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
