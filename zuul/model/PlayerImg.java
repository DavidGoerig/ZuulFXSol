package zuul.model;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Random;

public class PlayerImg {
    private String paths = "zuul/views/res/character.png";
    private Image image;

    /**
     * player img class
     */
    public PlayerImg() {
        this.image = new Image(paths);
    }

    /**
     * img getter
     * @return img
     */
    public Image getImage() {
        return image;
    }
}