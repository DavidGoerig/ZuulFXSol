package zuul.model;

import javafx.scene.image.Image;
import java.awt.*;
import java.util.Random;

public class Item {
    private String[] paths = {"zuul/views/res/avocado.png", "zuul/views/res/carrot.png", "zuul/views/res/broccoli.png"};
    private Point pos;
    private Image image;

    public Item(int gridx, int gridy, MovingPlayer movingPlayer) {
        this.image = new Image(randomImage(paths));
        int posx;
        int posy;
        do {
            Random random = new Random();
            posx = random.nextInt(gridx);
            posy = random.nextInt(gridy);
            pos = new Point(posx, posy);
        } while (movingPlayer.getHead().equals(pos));
    }

    protected String randomImage(String[] paths) {
        Random random = new Random();
        return paths[random.nextInt(paths.length)];
    }

    public Point getPos() {
        return new Point(pos);
    }

    public Image getImage() {
        return image;
    }
}