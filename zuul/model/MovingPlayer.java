package zuul.model;

import zuul.Item;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MovingPlayer {
    private final Point head = new Point();
    private final int x;
    private final int y;

    public MovingPlayer(int x, int y) {
        this.head.setLocation(x / 2, y / 2);
        this.x = x;
        this.y = y;
    }

    public void move(char dir) {
        Point currentLocation = head.getLocation();
        System.out.println(currentLocation);

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

        System.out.println(currentLocation);
        if (hitWalls()) {
            head.setLocation(currentLocation);
        }
    }

    public String takeItem(HashMap <Item, ItemDraw> items) {
        Iterator iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            ItemDraw item = (ItemDraw) me2.getValue();
            if (head.equals(item.getPos())) {
                Item itemOn = (Item) me2.getKey();
                return itemOn.getDescription();
            }
        }
        // TROUVER QUEL ITEM C, le take
        return null;
    }

    private boolean hitWalls() {
        return head.getX() < 0 || head.getY() < 0 || head.getX() >= x || head.getY() >= y;
    }

    public Point getHead() {
        return new Point(head);
    }

    public String goAway(HashMap<String, Exit> exits) {
        Iterator iterator = exits.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Exit exit = (Exit) me2.getValue();
            if (head.equals(exit.getPos())) {
                System.out.println("ON EST SORTIE: " + me2.getKey());
                String dir = (String) me2.getKey();
                return dir;
            }
        }
        return null;
    }

    public void moveOpposite(String dirToMove) {
        switch (dirToMove) {
            case "south":
                head.translate(0, -1);
                break;

            case "north":
                head.translate(0, 1);
                break;

            case "east":
                head.translate(-1, 0);
                break;

            case "west":
                head.translate(1, 0);
                break;
        }
    }
}