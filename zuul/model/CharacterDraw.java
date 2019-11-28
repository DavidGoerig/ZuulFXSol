package zuul.model;

import javafx.scene.image.Image;
import java.awt.*;
import java.util.Random;

public class CharacterDraw {
    private Point pos;
    private Image image;

    /**
     * Class for drawing characters
     * @param gridx x
     * @param gridy y
     */
    public CharacterDraw(int gridx, int gridy) {
        String path = "zuul/views/res/pika.png";
        this.image = new Image(path);
        pos = randomPos(gridx, gridy);
    }

    /**
     *  find a random pos between included in the grid
     * @param gridx grid x size
     * @param gridy grid y size
     * @return a random point
     */
    private Point randomPos(int gridx, int gridy) {
        Random random = new Random();
        int posx;
        int posy;
        posx = random.nextInt(gridx);
        posy = random.nextInt(gridy);
        Point rpos = new Point(posx, posy);
        return rpos;
    }

    /**
     * get pos
     * @return pos
     */
    public Point getPos() {
        return new Point(pos);
    }

    /**
     * get image
     * @return image
     */
    public Image getImage() {
        return image;
    }
}