package zuul.model;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Random;

public class Exit {
    private String paths = "zuul/views/res/avocado.png";
    private Point pos = new Point();
    private Image image;

    public Exit(int gridx, int gridy, MovingPlayer movingPlayer) {
        this.image = new Image(paths);
        pos.setLocation(gridx, gridy);
        int posy;
    }
    public Point getPos() {
        return new Point(pos);
    }

    public Image getImage() {
        return image;
    }
}