package zuul.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import zuul.model.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameView {
    private Canvas gridCanvas;
    private GraphicsContext gc;

    private Label gameOverLabel;
    private Label scoreLabel;
    private Label timer;
    private boolean showGameOver = false;

    private Game game;
    private TextField userName;
    private final int scale = 20;

    public GameView(Game game) {
        this.game = game;
    }

    public void makeScene(HashMap<String, Exit> exits, Scene scene, MovingPlayer movingPlayer, Item item) {
        gridCanvas = new Canvas(scene.getWidth() - 200, scene.getHeight());
        gc = gridCanvas.getGraphicsContext2D();
        drawGrid(exits, item, movingPlayer, gc);

        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setVisible(false);

        scoreLabel = new Label("Score: " + "Yeah");
        scoreLabel.setFont(new Font("Arial", 20));

        timer = new Label("Time: ");
        timer.setFont(new Font("Arial", 20));

        userName = new TextField("Name");
        userName.setVisible(false);
        userName.setAlignment(Pos.CENTER);
        userName.setMaxWidth(200);
    }

    public void drawGrid(HashMap<String, Exit> exits, Item item, MovingPlayer movingPlayer, GraphicsContext gc) {
        gc.setFill(Color.web("#dedede"));
        gc.fillRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        drawExits(exits);
        drawFood(item);
        drawHead(movingPlayer);
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

    private void drawFood(Item item) {
        gc.drawImage(item.getImage(), item.getPos().x * scale, item.getPos().y * scale,
                scale + 5, scale + 5);

    }

    private void drawHead(MovingPlayer movingPlayer) {
        gc.setFill(Color.web("#276324"));
        PlayerImg playerImg = new PlayerImg();
        gc.drawImage(playerImg.getImage(), movingPlayer.getHead().x * scale, movingPlayer.getHead().y * scale,
                scale +8, scale +8)
        ;

    }

    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + "Yes");
    }

    public void updateTimerLabel(String time) {
        timer.setText("Time: " + time);
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