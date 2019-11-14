package zuul.views;

import app.model.HighScore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import app.MainController;

public class HighScoreView {
  public HighScoreView(ScrollPane sp, Pane root) {
    root.setPadding(new Insets(30));

    Button backBtn = new Button("<- Back");
    backBtn.setOnAction(ev -> MainController.primaryStage.setScene(MainController.mainScene));

    Text title = new Text();
    title.setText(" HIGH SCORE \n");
    title.setFont(Font.font("Comic Sans MS", 28));
    title.setFill(Color.WHITE);

    Pane scoresRoot = new StackPane();
    sp.setContent(scoresRoot);
    sp.setPannable(true);
    sp.setFitToWidth(true);

    BackgroundFill myBF = new BackgroundFill(Color.web("333333"), CornerRadii.EMPTY, Insets.EMPTY);
    sp.setBackground(new Background(myBF));
    root.setBackground(new Background(myBF));
    scoresRoot.setBackground(new Background(myBF));

    HighScore highScore = new HighScore();
    highScore.readHighScoreFromFile();
    StringBuilder scores = new StringBuilder();

    highScore.getAllScores().forEach(el -> {
      scores.append(el[0]).append("\t-\t").append(el[1]).append("\n");
    });

    Text scoresText = new Text();
    scoresText.setFill(Color.WHITE);
    scoresText.setText("" + scores);
    scoresText.setFont(Font.font("Arial", 16));

    scoresRoot.getChildren().add(scoresText);

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(backBtn, title, sp);
    root.getChildren().addAll(vbox);
  }
}