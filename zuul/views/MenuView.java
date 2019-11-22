/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  The exits are labelled north,
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */

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
  private Button btnFr = new Button("Game in fr");
  private Button btnEn = new Button("Game in en");

  /**
   * set root Pane size padding and bg
   * @param rootPane Pane of the
   */
  private void setRootPane(Pane rootPane) {
    rootPane.setPrefSize(800, 1000);
    rootPane.setPadding(new Insets(20));
    BackgroundFill myBF = ViewUtils.createBg();
    rootPane.setBackground(new Background(myBF));
  }

  /**
   * set root Pane size padding and bg
   * @param rootPane root Pane
   */

  public MenuView(Pane rootPane) {
    this.setRootPane(rootPane);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);

    Text title = ViewUtils.createTitles("World of Zuul", 45);
    Text subTitle = ViewUtils.createTitles("Created with Java FX", 22);
    Text authTitle = ViewUtils.createTitles("Author: David GOERIG", 12);

    VBox.setMargin(title, new Insets(0, 0, 50, 0));
    VBox.setMargin(subTitle, new Insets(0, 0, 30, 0));
    VBox.setMargin(authTitle, new Insets(200, 50, 0, 200));

    VBox buttons = new VBox();
    buttons.setSpacing(30);
    buttons.setAlignment(Pos.CENTER);

    ArrayList<Button> btns = new ArrayList<>();
    btns.addAll(Arrays.asList(btnStart, btnSetMap, btnExit, btnEn, btnFr));

    btns.forEach(el -> {
      el.setPrefWidth(200.0);
      el.setPrefHeight(50.0);
      buttons.getChildren().add(el);
    });

    vbox.getChildren().addAll(title, subTitle, buttons, authTitle);
    rootPane.getChildren().addAll(vbox);
  }

  public Button btnStart() {
    return btnStart;
  }

  public Button getBtnSetMap() {
    return btnSetMap;
  }

  public Button btnExit() {
    return btnExit;
  }

  public Button getBtnFr() {
    return btnFr;
  }

  public Button getBtnEn() {
    return btnEn;
  }
}