package app.model;

import javafx.scene.image.Image;
import java.awt.*;
import java.util.Random;

public class Food {
    private String[] paths = {"zuul/views/res/avocado.png", "zuul/views/res/carrot.png", "zuul/views/res/broccoli.png"};
    private Point pos;
    private Image image;

    public Food(int gridx, int gridy, Snake snake) {
        this.image = new Image(randomImage(paths));
        int posx;
        int posy;
        do {
            Random random = new Random();
            posx = random.nextInt(gridx);
            posy = random.nextInt(gridy);
            pos = new Point(posx, posy);
        } while (snake.getHead().equals(pos) || snake.getBody().contains(pos));
    }

    protected String randomImage(String[] paths) {
        Random random = new Random();
        return paths[random.nextInt(paths.length)];
    }

    public Point getPos() {
        return new Point(pos);
    }

    public boolean decreaseLength() {
        return false;
    }

    public Image getImage() {
        return image;
    }
}