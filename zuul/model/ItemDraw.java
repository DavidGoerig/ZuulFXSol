package zuul.model;

import javafx.scene.image.Image;
import java.awt.*;
import java.util.Random;

public class ItemDraw {
    private String[] paths = {"zuul/views/res/avocado.png", "zuul/views/res/carrot.png", "zuul/views/res/broccoli.png"};
    private Point pos;
    private Image image;

    public ItemDraw(int gridx, int gridy, MovingPlayer movingPlayer) {
        this.image = new Image(randomImage(paths));
        pos = randomPos(gridx, gridy);
    }

    private Point randomPos(int gridx, int gridy) {
        Random random = new Random();
        int posx;
        int posy;
        posx = random.nextInt(gridx);
        posy = random.nextInt(gridy);
        Point rpos = new Point(posx, posy);
        return rpos;
    }

    private String randomImage(String[] paths) {
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