package zuul.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import zuul.Game;
import zuul.Item;
import zuul.model.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameView {
    private Canvas gridCanvas;
    private GraphicsContext gc;

    private Label roomNameLabel;
    private Label roomDescLabel;

    private Game game;
    private Label userName;
    private final int scale = 20;

    public GameView(Game game) {
        this.game = game;
    }

    public void makeScene(HashMap<String, Exit> exits, Scene scene, MovingPlayer movingPlayer, HashMap<Item, ItemDraw> items, List<CharacterDraw> characters) {
        gridCanvas = new Canvas(scene.getWidth() - 400, scene.getHeight());
        gc = gridCanvas.getGraphicsContext2D();
        drawGrid(items, exits, movingPlayer, gc, characters);

        roomNameLabel = labelCreator(game.getPlayer().getCurrentRoom().getName());
        roomDescLabel = labelCreator(game.getPlayer().getCurrentRoom().getDescription());
        userName = labelCreator(game.getPlayer().getName());
    }

    private Label labelCreator(String txt) {
        Label label = new Label(txt);
        label.setFont(new Font("Verdana", 15));
        label.setTextFill(Color.WHITE);
        return label;
    }

    public void drawGrid(HashMap<Item, ItemDraw> items, HashMap<String, Exit> exits, MovingPlayer movingPlayer, GraphicsContext gc, List<CharacterDraw> characters) {
        gc.setFill(Color.web("#dedede"));
        gc.fillRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        drawExits(exits);
        drawItems(items);
        drawHead(movingPlayer);
        drawCharacters(characters);
    }

    private void drawCharacters(List<CharacterDraw> characters) {
        for (CharacterDraw i : characters) {
            gc.drawImage(i.getImage(), i.getPos().x * scale, i.getPos().y * scale,
                    scale + 5, scale + 5);
        }

    }

    private void drawExits(HashMap<String, Exit> exits) {
        Iterator iterator = exits.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Exit item =  (Exit) me2.getValue();
            gc.drawImage(item.getImage(), item.getPos().x * scale, item.getPos().y * scale,
                    scale + 5, scale + 5);
        }
    }

    private void drawItems(HashMap <Item, ItemDraw> items) {
        Iterator iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            ItemDraw item = (ItemDraw) me2.getValue();
            gc.drawImage(item.getImage(), item.getPos().x * scale, item.getPos().y * scale,
                    scale + 5, scale + 5);
        }
    }

    private void drawHead(MovingPlayer movingPlayer) {
        gc.setFill(Color.web("#276324"));
        PlayerImg playerImg = new PlayerImg();
        gc.drawImage(playerImg.getImage(), movingPlayer.getHead().x * scale, movingPlayer.getHead().y * scale,
                scale +8, scale +8)
        ;

    }

    public void updateRoomNameLabel(String name) {
        roomNameLabel.setText(name);
    }

    public void roomDescLabel(String desc) {
        roomDescLabel.setText(desc);
    }

    public Canvas getGridCanvas() {
        return gridCanvas;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public Label getRoomNameLabel() {
        return roomNameLabel;
    }

    public Label getRoomDescLabel() {
        return roomDescLabel;
    }

    public Label getUserName() {
        return userName;
    }
}