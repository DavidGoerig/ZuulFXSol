package zuul.views;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuView {
  private Button btnStart = new Button("New Game");
  private Button btnScores = new Button("High Score");
  private Button btnExit = new Button("Exit");

  public MenuView(Pane root) {
    root.setPrefSize(700, 700);
    root.setPadding(new Insets(20));
    BackgroundFill myBF = new BackgroundFill(Color.web("333333"), CornerRadii.EMPTY, Insets.EMPTY);
    root.setBackground(new Background(myBF));

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);

    Text title = new Text();
    title.setText("Snake Game");

    title.setFont(Font.font("Comic Sans MS", 28));
    title.setFill(Color.WHITE);

    VBox.setMargin(title, new Insets(0, 0, 50, 0));

    VBox buttons = new VBox();
    buttons.setSpacing(20);
    buttons.setAlignment(Pos.CENTER);

    ArrayList<Button> btns = new ArrayList<>();
    btns.addAll(Arrays.asList(btnStart, btnScores, btnExit));

    btns.forEach(el -> {
      el.setPrefWidth(150.0);
      el.setPrefHeight(40.0);
      buttons.getChildren().add(el);
    });

    vbox.getChildren().addAll(title, buttons);
    root.getChildren().addAll(vbox);
  }

  public Button btnStart() {
    return btnStart;
  }

  public Button btnScores() {
    return btnScores;
  }

  public Button btnExit() {
    return btnExit;
  }
}