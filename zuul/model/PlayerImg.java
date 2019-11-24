package zuul.model;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Random;

public class PlayerImg {
    private String paths = "zuul/views/res/character.png";
    private Image image;

    public PlayerImg() {
        this.image = new Image(paths);
    }

    public Image getImage() {
        return image;
    }
}