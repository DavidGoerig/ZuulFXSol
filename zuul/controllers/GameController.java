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

/**
 *  Game controller
 */
public class GameController {
    private MovingPlayer movingPlayer;
    private HBox buttonMapBox = new HBox();
    private HashMap <Item, ItemDraw> items;
    private GameView gameView;
    private GameBoard gameBoard;
    private Game game;
    private List<CharacterDraw> characters = new ArrayList<>();
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
        gameView.createTables();
        createExits();
        animation.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * exits getter
     * @return exits
     */
    public HashMap<String, Exit> getExits() {
        return exits;
    }

    /**
     * items getter
     * @return items
     */
    public HashMap<Item, ItemDraw> getItems() {
        return items;
    }

    /**
     * button map box getter
     * @return button map box
     */
    public HBox getButtonMapBox() {
        return buttonMapBox;
    }


    /**
     * exits creation at the good position in the window
     */
    private void createExits() {
        exits.put("west", new Exit(0, 15, "west"));
        exits.put("north", new Exit(15, 0, "north"));
        exits.put("south", new Exit(15, 29, "south"));
        exits.put("east", new Exit(29, 15, "east"));
    }

    /**
     *  method to move the player in each direction
     */
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

    /**
     *  method to check if we can take the item (if the player is on it), create an alert otherwise
     */
    private void checkItem() {
        String itemOn = movingPlayer.takeItem(items);
        if (itemOn != null) {
            if (!game.getPlayer().take(itemOn)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(Game.messages.getString("alTitle"));
                alert.setHeaderText(Game.messages.getString("alHeader"));
                alert.setContentText(Game.messages.getString("alContent"));
                alert.show();
            }
            items = createItem();
            gameView.updateDatas();
        }
    }

    /**
     * method to check exits (if the player is on it), create an exit otherwise
     */
    private void checkExit() {
        String dirToMove = movingPlayer.goAway(exits);
        if (dirToMove != null) {
            if (game.getPlayer().goRoom(dirToMove)) {
                reset();
            } else {
                movingPlayer.moveOpposite(dirToMove);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(Game.messages.getString("aleTitle"));
                alert.setHeaderText(Game.messages.getString("aleHeader"));
                alert.setContentText(Game.messages.getString("aleContent1") + dirToMove +" " +Game.messages.getString("aleContent2"));
                alert.show();
            }
        }
    }

    /**
     * animation timeline, execute these methods each frames
     */
    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
        movePlayer();
        checkItem();
        checkExit();
        gameView.drawGrid(items, exits, movingPlayer, gameView.getGraphicsContext(), characters);
        updateLabel();
        checkGameViewGridUpdate();
        gameView.getGridCanvas().requestFocus();
    }));

    /**
     * check if we nned to update the grid or not (MVC pattern)
     */
    private void checkGameViewGridUpdate() {
        if (gameView.isUpdateinGameView()) {
            items = createItem();
            updateLabel();
            gameView.setUpdateinGameView(false);
        }
    }

    /**
     * Method for updating labels
     */
    private void updateLabel() {
        gameView.updateRoomNameLabel(game.getPlayer().getCurrentRoom().getName());
        gameView.roomDescLabel(game.getPlayer().getCurrentRoom().getDescription());
    }

    /**
     *  handle method, to do action depending on the pressed key
     * @param event pressed key
     */
    void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        animation.play();
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

    /**
     * check if the pressed key is a valid one (up, down, left, right)
     * @param key pressed key
     * @return true if it is a arrow key
     */
    private boolean validKey(KeyCode key) {
        return (key.equals(KeyCode.LEFT) && !key().equals(KeyCode.RIGHT))
                || (key.equals(KeyCode.RIGHT) && !key().equals(KeyCode.LEFT))
                || (key.equals(KeyCode.DOWN) && !key().equals(KeyCode.UP))
                || (key.equals(KeyCode.UP) && !key().equals(KeyCode.DOWN));
    }

    /**
     *  reset / refresh the pane and his component
     */
    private void reset() {

        updateButtonMapChanging();
        animation.jumpTo(Duration.ZERO);
        animation.stop();

        gameView.updateDatas();
        movingPlayer = new MovingPlayer(gameBoard.getWidth(), gameBoard.getHeight());
        items = createItem();
        characters = createCharacters();
        updateLabel();
        gameView.drawGrid(items, exits, movingPlayer, gameView.getGraphicsContext(), characters);
        gameView.getGridCanvas().requestFocus();
    }

    /**
     * update button for changing the map (north, south etc)
     */
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
                    gameView.updateDatas();
                });
                buttonMapBox.getChildren().addAll(b);
            }
        }
    }

    /**
     * method for creating characters to draw (link between char and the draw)
     * @return list of character to draw
     */
    private List<CharacterDraw> createCharacters() {
        List<CharacterDraw> al = new ArrayList<>();
        Map<String, Character> allChar = new HashMap<>(game.getPlayer().getCurrentRoom().getCharacters());

        for (Object value : allChar.values()) {
            al.add(new CharacterDraw(gameBoard.getWidth(), gameBoard.getHeight()));
        }
        return al;
    }

    /**
     * method for creating items to draw (link between items and the draw)
     * @return list of items to draw
     */
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

    /**
     * get key
     * @return key
     */
    private static KeyCode key() {
        return key;
    }

    /**
     * get char
     * @return char
     */
    public List<CharacterDraw> getCharacters() {
        return  characters;
    }
}