package zuul.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import zuul.model.Exit;
import zuul.model.Item;
import zuul.model.Game;
import zuul.model.MovingPlayer;
import zuul.views.GameView;

import java.util.HashMap;

public class GameController {
    private MovingPlayer movingPlayer;
    private Item item;
    private GameView gameView;
    private Game game;

    public HashMap<String, Exit> getExits() {
        return exits;
    }

    private HashMap<String, Exit> exits;

    public static KeyCode key = KeyCode.W;
    private boolean started = false;

    public GameController(MovingPlayer movingPlayer, Item item, Game game, GameView gameView) {
        this.movingPlayer = movingPlayer;
        this.item = item;
        this.gameView = gameView;
        this.game = game;
        this.exits = new HashMap<String, Exit>();
        exits.put("west", new Exit(0, 15, movingPlayer));
        exits.put("north", new Exit(15, 0, movingPlayer));
        exits.put("south", new Exit(15, 29, movingPlayer));
        exits.put("east", new Exit(29, 15, movingPlayer));

        animation.setCycleCount(Animation.INDEFINITE);
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
        if (true) {
            gameView.updateTimerLabel("YES HOMIE");

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

            if (movingPlayer.takeItem(item)) {
                System.out.println("LA ON EST SUR UN ITEM");
                // Ici juste take l'item, et trouver lequel
                newFood();
            }
            if (movingPlayer.goAway(exits)) {

            }
            gameView.drawGrid(exits, item, movingPlayer, gameView.getGraphicsContext());
            gameView.updateScoreLabel();
        }
    }));

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

        movingPlayer = new MovingPlayer(game.getWidth(), game.getHeight());
        item = new Item(game.getWidth(), game.getHeight(), movingPlayer);

        key = KeyCode.LEFT;
        gameView.updateScoreLabel();

        gameView.drawGrid(exits, item, movingPlayer, gameView.getGraphicsContext());
    }

    private void newFood() {
        new Item(game.getWidth(), game.getHeight(), movingPlayer);
    }

    private static KeyCode key() {
        return key;
    }

}