package app.model;

import javafx.scene.image.Image;

public class FastFood extends Food {
    private String[] paths = {"zuul/views/res/pizza.png", "zuul/views/res/french-fries.png"};
    private Image image;

    public FastFood(int x, int y, Snake snake) {
        super(x, y, snake);
        this.image = new Image(super.randomImage(paths));
    }

    public boolean decreaseLength() {
        return true;
    }

    public Image getImage() {
        return image;
    }
}
