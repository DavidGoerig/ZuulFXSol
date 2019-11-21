package zuul.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.Point;

import zuul.model.Food;
import zuul.model.Game;
import zuul.model.Snake;

public class GameView {
    private Canvas gridCanvas;
    private GraphicsContext gc;

    private Label gameOverLabel;
    private Label scoreLabel;
    private Label timer;
    private boolean showGameOver = false;

    private Game game;
    private TextField userName;

    public GameView(Game game) {
        this.game = game;
    }

    public void makeScene(Scene scene, Snake snake, Food food) {
        gridCanvas = new Canvas(scene.getWidth() - 200, scene.getHeight());
        gc = gridCanvas.getGraphicsContext2D();
        drawGrid(food, snake, gc);

        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setVisible(false);

        scoreLabel = new Label("Score: " + game.getScorePoints());
        scoreLabel.setFont(new Font("Arial", 20));

        timer = new Label("Time: ");
        timer.setFont(new Font("Arial", 20));

        // resetBtn = new Button("Reset");

        userName = new TextField("Name");
        userName.setVisible(false);
        userName.setAlignment(Pos.CENTER);
        userName.setMaxWidth(200);
    }

    public void drawGrid(Food food, Snake snake, GraphicsContext gc) {
        gc.setFill(Color.web("#dedede"));
        gc.fillRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        drawFood(food);
        snake.getBody().forEach(point -> drawBody(snake, point));
        drawHead(snake);
    }

    private void drawFood(Food food) {
        gc.drawImage(food.getImage(), food.getPos().x * game.getScale(), food.getPos().y * game.getScale(),
                game.getScale() + 5, game.getScale() + 5);

    }

    private void drawHead(Snake snake) {
        gc.setFill(Color.web("#276324"));
        gc.fillRect(snake.getHead().x * game.getScale() + 1.25, snake.getHead().y * game.getScale() + 1.25,
                game.getScale() - 2.5, game.getScale() - 2.5);

    }

    private void drawBody(Snake snake, Point point) {
        gc.setFill(Color.web("#62a85f"));
        gc.fillRect(point.x * game.getScale() + 1.25, point.y * game.getScale() + 1.25, game.getScale() - 2.5,
                game.getScale() - 2.5);
    }

    public void gameOver(Snake snake) {
        if (snake.isDead() && !showGameOver) {
            showGameOver = true;
            gameOverLabel.setVisible(true);
            saveScore();
        } else if (!snake.isDead() && showGameOver) {
            gameOverLabel.setVisible(false);
            showGameOver = false;
        }
    }

    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + game.getScorePoints());
    }

    public void updateTimerLabel(String time) {
        timer.setText("Time: " + time);
    }

    public void saveScore() {
        userName.setVisible(true);
        userName.setOnAction(ev -> {
            userName.setVisible(false);
//            highScore.addScore(userName.getText(), "" + game.getScorePoints());
        });
    }

    public void setTimer(String time) {
        timer.setText(time);
    }

    public Canvas getGridCanvas() {
        return gridCanvas;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public Label getGameOverLabel() {
        return gameOverLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getTimerLabel() {
        return timer;
    }

    public TextField getUserName() {
        return userName;
    }
}