package zuul.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import zuul.model.*;
import zuul.model.FastFood;
import zuul.model.Food;
import zuul.model.Game;
import zuul.model.Snake;
import zuul.views.GameView;

public class GameController {
    private Snake snake;
    private Food food;
    private GameView gameView;
    private Game game;

    public static KeyCode key = KeyCode.LEFT;
    private boolean started = false;

    private Timer timer;
    private Thread thread;
    private double timepoints;

    public GameController(Snake snake, Food food, Game game, GameView gameView) {
        this.snake = snake;
        this.food = food;
        this.gameView = gameView;
        this.game = game;

        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(game.getSpeed());

        timer = new Timer();
        thread = new Thread(timer);
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        if (!snake.isDead()) {
            double newTimepoints = timer.getCounter() * game.getTimeWeight() / (game.getWidth() * game.getHeight());
            newTimepoints = Math.round(newTimepoints * 100.0 / 100.0);
            if (timepoints != newTimepoints) {
                timepoints = newTimepoints;
                game.addPoints(timepoints);
            }
            gameView.updateTimerLabel("" + timer.getCounter());

            if (key.equals(KeyCode.LEFT)) {
                snake.move('L');
            } else if (key.equals(KeyCode.RIGHT)) {
                snake.move('R');
            } else if (key.equals(KeyCode.UP)) {
                snake.move('U');
            } else if (key.equals(KeyCode.DOWN)) {
                snake.move('D');
            }

            if (snake.ateFood(food)) {
                game.addPoints();
                newFood();
            }

            if (!snake.hitSelf()) {
                gameView.drawGrid(food, snake, gameView.getGraphicsContext());
            }

            gameView.gameOver(snake);
            gameView.updateScoreLabel();
        }
    }));

    public void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.isArrowKey() && !started) {
            animation.play();
            thread.start();
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
        timer.doStop();
        started = false;
        animation.jumpTo(Duration.ZERO);
        animation.stop();

        snake = new Snake(game.getWidth(), game.getHeight());
        food = new Food(game.getWidth(), game.getHeight(), snake);

        animation.setRate(game.getSpeed());
        key = KeyCode.LEFT;
        game.resetScore();

        gameView.gameOver(snake);
        gameView.updateScoreLabel();

        gameView.drawGrid(food, snake, gameView.getGraphicsContext());
    }

    private void newFood() {
        int ran = (int) (Math.random() * 5);
        food = ran > 0 ? new Food(game.getWidth(), game.getHeight(), snake)
                : new FastFood(game.getWidth(), game.getHeight(), snake);
    }

    private static KeyCode key() {
        return key;
    }

}