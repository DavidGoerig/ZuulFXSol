package zuul.model;

import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private boolean dead = false;
    private final Point head = new Point();
    private final ArrayList<Point> body = new ArrayList<>();
    private final int x;
    private final int y;

    public Snake(int x, int y) {
        this.head.setLocation(x / 2, y / 2);
        this.body.add(new Point(x / 2, y / 2 + 1));
        this.x = x;
        this.y = y;
    }

    public void move(char dir) {
        body.add(new Point(head));
        Point currentLocation = head.getLocation();

        switch (dir) {
        case 'U':
            head.translate(0, -1);
            break;

        case 'D':
            head.translate(0, 1);
            break;

        case 'L':
            head.translate(-1, 0);
            break;

        case 'R':
            head.translate(1, 0);
            break;
        }

        if (hitSelf() || hitWalls()) {
            head.setLocation(currentLocation);
            dead = true;
        }
    }

    public boolean ateFood(Food food) {
        boolean ate = head.equals(food.getPos());
        if (!ate) {
            body.remove(0);
        } else if (food.decreaseLength()) {
            body.remove(0);
            if (body.size() > 1) {
                body.remove(0);
            }
        }
        return ate;
    }

    private boolean hitWalls() {
        return head.getX() < 0 || head.getY() < 0 || head.getX() >= x || head.getY() >= y;
    }

    public boolean hitSelf() {
        return body.contains(head);
    }

    public ArrayList<Point> getBody() {
        return new ArrayList<>(body);
    }

    public Point getHead() {
        return new Point(head);
    }

    public boolean isDead() {
        return dead;
    }
}