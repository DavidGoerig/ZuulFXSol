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
  private Button btnSetMap = new Button("Set Map");
  private Button btnExit = new Button("Exit");

  private void setRootPane(Pane menuViewPane) {
    menuViewPane.setPrefSize(600, 800);
    menuViewPane.setPadding(new Insets(20));
    BackgroundFill myBF = new BackgroundFill(Color.web("420d42"), CornerRadii.EMPTY, Insets.EMPTY);
    menuViewPane.setBackground(new Background(myBF));
  }

  private Text createTitles(String text, int size) {
    Color color = Color.WHITE;
    Font titleFont = Font.font("Verdana", size);
    Text title = new Text();
    title.setText(text);
    title.setFont(titleFont);
    title.setFill(color);
    return title;
  }

  public MenuView(Pane menuViewPane) {
    this.setRootPane(menuViewPane);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);

    Text title = createTitles("World of Zuul", 45);
    Text subTitle = createTitles("Created with Java FX", 22);
    Text authTitle = createTitles("Author: David GOERIG", 12);

    VBox.setMargin(title, new Insets(0, 0, 50, 0));
    VBox.setMargin(subTitle, new Insets(0, 0, 30, 0));
    VBox.setMargin(authTitle, new Insets(200, 50, 0, 200));

    VBox buttons = new VBox();
    buttons.setSpacing(30);
    buttons.setAlignment(Pos.CENTER);

    ArrayList<Button> btns = new ArrayList<>();
    btns.addAll(Arrays.asList(btnStart, btnSetMap, btnExit));

    btns.forEach(el -> {
      el.setPrefWidth(200.0);
      el.setPrefHeight(50.0);
      buttons.getChildren().add(el);
    });

    vbox.getChildren().addAll(title, subTitle, buttons, authTitle);
    menuViewPane.getChildren().addAll(vbox);
  }

  public Button btnStart() {
    return btnStart;
  }

  public Button btnScores() {
    return btnSetMap;
  }

  public Button btnExit() {
    return btnExit;
  }
}