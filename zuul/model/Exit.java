package zuul.model;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Exit {
    private Point pos = new Point();
    private Image image;

    public Exit(int gridx, int gridy, String dir) {
        HashMap<String, String> paths = new HashMap<>();
        paths.put("north", "zuul/views/res/arrow_up.png");
        paths.put("south", "zuul/views/res/arrow_down.png");
        paths.put("east", "zuul/views/res/arrow_right.png");
        paths.put("west", "zuul/views/res/arrow_left.png");
        this.image = new Image(paths.get(dir));
        pos.setLocation(gridx, gridy);
    }
    public Point getPos() {
        return new Point(pos);
    }

    public Image getImage() {
        return image;
    }
}