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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import zuul.Game;

public class MenuView {
  private Button btnStart = new Button(Game.messages.getString("btnStart"));
  private Button btnSetMap = new Button(Game.messages.getString("btnSetMap"));
  private Button btnExit = new Button(Game.messages.getString("btnExit"));
  private Button btnFr = new Button(Game.messages.getString("btnFr"));
  private Button btnEn = new Button(Game.messages.getString("btnEn"));
  private Text title;
  private Text subTitle;
  private Text authTitle;

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


  private void setTitles() {
    title = ViewUtils.createTitles(Game.messages.getString("title"), 45);
    subTitle = ViewUtils.createTitles(Game.messages.getString("subTitle"), 22);
    authTitle = ViewUtils.createTitles(Game.messages.getString("authTitle"), 12);
  }
  /**
   * set root Pane size padding and bg
   * @param rootPane root Pane
   */

  public MenuView(Pane rootPane) {
    this.setRootPane(rootPane);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    Image enImg = new Image("zuul/views/res/en.png");
    btnEn.setGraphic(new ImageView(enImg));

    Image frImg = new Image("zuul/views/res/fr.png");
    btnFr.setGraphic(new ImageView(frImg));
    setTitles();

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

  /**
   * get btn start
   * @return button start obj
   */
  public Button getBtnStart() {
    return btnStart;
  }

  /**
   * get button to got to set map
   * @return button
   */
  public Button getBtnSetMap() {
    return btnSetMap;
  }

  /**
   * get button to exit
   * @return button
   */
  public Button getBtnExit() {
    return btnExit;
  }

  /**
   * get button fr
   * @return button
   */
  public Button getBtnFr() {
    return btnFr;
  }

  /**
   * get button en
   * @return button
   */
  public Button getBtnEn() {
    return btnEn;
  }

  /**
   * update all buttons text
   */
  public void updateButtonTextDisplay() {
    btnStart.setText(Game.messages.getString("btnStart"));
    btnSetMap.setText(Game.messages.getString("btnSetMap"));
    btnExit.setText(Game.messages.getString("btnExit"));
    btnFr.setText(Game.messages.getString("btnFr"));
    btnEn.setText(Game.messages.getString("btnEn"));
    title.setText(Game.messages.getString("title"));
    subTitle.setText(Game.messages.getString("subTitle"));
    authTitle.setText(Game.messages.getString("authTitle"));

  }
}