package zuul.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import zuul.Game;
import zuul.Item;
import zuul.model.Exit;
import zuul.model.GameBoard;
import zuul.model.ItemDraw;
import zuul.model.MovingPlayer;
import zuul.views.GameView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameController {
    private MovingPlayer movingPlayer;

    public HashMap<Item, ItemDraw> getItems() {
        return items;
    }

    private HashMap <Item, ItemDraw> items;
    private GameView gameView;
    private GameBoard gameBoard;
    private Game game;

    public HashMap<String, Exit> getExits() {
        return exits;
    }

    private HashMap<String, Exit> exits;

    private static KeyCode key = KeyCode.W;
    private boolean started = false;

    public GameController(MovingPlayer movingPlayer, GameBoard gameBoard, GameView gameView, Game game) {
        this.game = game;
        this.movingPlayer = movingPlayer;
        this.gameView = gameView;
        this.gameBoard = gameBoard;
        this.exits = new HashMap<String, Exit>();
        items = createItem();
        exits.put("west", new Exit(0, 15, "west"));
        exits.put("north", new Exit(15, 0, "north"));
        exits.put("south", new Exit(15, 29, "south"));
        exits.put("east", new Exit(29, 15, "east"));

        animation.setCycleCount(Animation.INDEFINITE);
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
        if (true) {
            if (key.equals(KeyCode.LEFT)) {
                movingPlayer.move('L');
                key = KeyCode.W;
            } else if (key.equals(KeyCode.RIGHT)) {
                movingPlayer.move('R');
                key = KeyCode.W;
            } else if (key.equals(KeyCode.UP)) {
                movingPlayer.move('U');
                key = KeyCode.W;
            } else if (key.equals(KeyCode.DOWN)) {
                movingPlayer.move('D');
                key = KeyCode.W;
            }

            if (movingPlayer.takeItem(items)) {
                System.out.println("LA ON EST SUR UN ITEM");
                // Ici juste take l'item, et trouver lequel
            }
            if (movingPlayer.goAway(exits)) {

            }
            gameView.drawGrid(items, exits, movingPlayer, gameView.getGraphicsContext());
            updateLabel();
        }
    }));

    private void updateLabel() {
        gameView.updateRoomNameLabel(game.getPlayer().getCurrentRoom().getName());
        gameView.roomDescLabel(game.getPlayer().getCurrentRoom().getDescription());
    }

    public void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.isArrowKey() && !started) {
            animation.play();
            started = true;
        }

        if (validKey(keyCode)) {
            key = event.getCode();
        }

        if (keyCode.equals(KeyCode.R)) {
            reset();
        }

        KeyCombination keyCombination = new KeyCodeCombination(KeyCode.T, KeyCombination.SHIFT_ANY,
                KeyCombination.SHORTCUT_DOWN);
        if (keyCombination.match(event)) {
            MainController.getGameStage().close();
            MainController.getPrimaryStage().show();
            MainController.getPrimaryStage().setScene(MainController.getMainScene());
        }

        if (keyCode.equals(KeyCode.ESCAPE)) {
            System.exit(0);
        }
    }

    private boolean validKey(KeyCode key) {
        return (key.equals(KeyCode.LEFT) && !key().equals(KeyCode.RIGHT))
                || (key.equals(KeyCode.RIGHT) && !key().equals(KeyCode.LEFT))
                || (key.equals(KeyCode.DOWN) && !key().equals(KeyCode.UP))
                || (key.equals(KeyCode.UP) && !key().equals(KeyCode.DOWN));
    }

    public void reset() {
        started = false;
        animation.jumpTo(Duration.ZERO);
        animation.stop();

        movingPlayer = new MovingPlayer(gameBoard.getWidth(), gameBoard.getHeight());
        items = createItem();
        key = KeyCode.LEFT;
        updateLabel();

        gameView.drawGrid(items, exits, movingPlayer, gameView.getGraphicsContext());
    }

    private HashMap<Item, ItemDraw> createItem() {
        Map<String, Item> itemFromRoom = game.getPlayer().getCurrentRoom().getItems();
        HashMap<Item, ItemDraw> itemFromRoomDraw = new HashMap<>();
        Iterator iterator = itemFromRoom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Item tmp = (Item) me2.getValue();
            itemFromRoomDraw.put(tmp, new ItemDraw(gameBoard.getWidth(), gameBoard.getHeight(), movingPlayer));
        }
        return itemFromRoomDraw;
    }

    private static KeyCode key() {
        return key;
    }

}