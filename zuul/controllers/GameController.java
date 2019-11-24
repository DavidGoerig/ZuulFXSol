package zuul.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import zuul.Character;
import zuul.Game;
import zuul.Item;
import zuul.model.*;
import zuul.room.Room;
import zuul.views.GameView;

import java.util.*;

public class GameController {
    private MovingPlayer movingPlayer;

    public HBox getButtonMapBox() {
        return buttonMapBox;
    }

    private HBox buttonMapBox = new HBox();
    public HashMap<Item, ItemDraw> getItems() {
        return items;
    }

    private HashMap <Item, ItemDraw> items;
    private GameView gameView;
    private GameBoard gameBoard;
    private Game game;
    private List<CharacterDraw> characters = new ArrayList<>();

    public HashMap<String, Exit> getExits() {
        return exits;
    }

    private HashMap<String, Exit> exits;

    private static KeyCode key = KeyCode.W;

    public GameController(MovingPlayer movingPlayer, GameBoard gameBoard, GameView gameView, Game game) {
        this.game = game;
        this.movingPlayer = movingPlayer;
        this.gameView = gameView;
        this.gameBoard = gameBoard;
        this.exits = new HashMap<String, Exit>();
        items = createItem();
        characters = createCharacters();
        exits.put("west", new Exit(0, 15, "west"));
        exits.put("north", new Exit(15, 0, "north"));
        exits.put("south", new Exit(15, 29, "south"));
        exits.put("east", new Exit(29, 15, "east"));
        animation.setCycleCount(Animation.INDEFINITE);
    }

    private void movePlayer() {
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
    }

    private void checkItem() {
        String itemOn = movingPlayer.takeItem(items);
        if (itemOn != null) {
            game.getPlayer().take(itemOn);
            items = createItem();
        }
    }

    private void checkExit() {
        String dirToMove = movingPlayer.goAway(exits);
        if (dirToMove != null) {
            if (game.getPlayer().goRoom(dirToMove)) {
                reset();
            } else {
                movingPlayer.moveOpposite(dirToMove);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No exit here sorry!");
                alert.setHeaderText("Exit not available.");
                alert.setContentText("No " + dirToMove +" exit");
                alert.show();
            }
        }
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
        movePlayer();
        checkItem();
        checkExit();
        gameView.drawGrid(items, exits, movingPlayer, gameView.getGraphicsContext(), characters);
        updateLabel();
        gameView.getGridCanvas().requestFocus();
    }));

    private void updateLabel() {
        gameView.updateRoomNameLabel(game.getPlayer().getCurrentRoom().getName());
        gameView.roomDescLabel(game.getPlayer().getCurrentRoom().getDescription());
    }

    void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.isArrowKey()) {
            animation.play();
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

    private void reset() {

        updateButtonMapChanging();
        animation.jumpTo(Duration.ZERO);
        animation.stop();

        movingPlayer = new MovingPlayer(gameBoard.getWidth(), gameBoard.getHeight());
        items = createItem();
        characters = createCharacters();
        updateLabel();
        gameView.drawGrid(items, exits, movingPlayer, gameView.getGraphicsContext(), characters);
        gameView.getGridCanvas().requestFocus();
    }

    void updateButtonMapChanging() {
        buttonMapBox.getChildren().clear();
        Map<String, Room> exits = game.getPlayer().getCurrentRoom().getExits();
        Iterator iterator = exits.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Room r = (Room) me2.getValue();
            String s = (String) me2.getKey();
            if (r != null) {
                Button b = new Button(s + ": " + r.getName());
                b.setOnAction(e -> {
                    game.getPlayer().goRoom(s);
                    reset();
                });
                buttonMapBox.getChildren().addAll(b);
            }
        }
    }

    private List<CharacterDraw> createCharacters() {
        List<CharacterDraw> al = new ArrayList<>();
        Map<String, Character> allChar = game.getPlayer().getCurrentRoom().getCharacters();
        Iterator iterator = allChar.entrySet().iterator();
        while (iterator.hasNext()) {
            al.add(new CharacterDraw(gameBoard.getWidth(), gameBoard.getHeight()));
        }
        return al;
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

    public List<CharacterDraw> getCharacters() {
        return  characters;
    }
}