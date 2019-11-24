package zuul.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import zuul.Item;
import zuul.model.ItemDraw;
import zuul.model.GameBoard;
import zuul.model.MovingPlayer;
import zuul.mygame.MyGame;
import zuul.room.Room;
import zuul.views.MenuView;
import zuul.views.GameView;
import zuul.views.SetMapView;
import zuul.views.SettingView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    // Primary stage / main scene
    public static Stage primaryStage = new Stage();
    public static Scene mainScene;

    /**
     * rootPane
     */
    private Pane rootPane = new StackPane();
    /** Setting Pane

     */
    private Pane settingsRoot = new StackPane();
    /**
     *  Set map Pane
     */
    private Pane setMapPaneRoot= new StackPane();
    private ScrollPane scoresScrollPane = new ScrollPane();

    private static Stage gameStage;
    private Scene gameScene;

    private static int X_VALUE;
    private static int Y_VALUE;


    // Jé fé ça
    private MyGame game = new zuul.mygame.MyGame("en", "us");
    private SetMapView setMapView = null;
    private SettingView settingView = null;
    private Scene sceneSetMapView;
    private Scene settingScene;
    private MenuView menuView;

    public MainController() throws IOException {
        initGameController();
    }

    private void initGameController() throws IOException {
        menuView = new MenuView(rootPane);
        mainScene = new Scene(rootPane);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Word of Zuul - DJG");
        primaryStage.show();

        initSetMapScene();
        setAllRooms();
        game.createPlayer("David");
        initSettingsScene();

        menuView.btnStart().setOnAction(e -> {
            try {
                setSettingScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menuView.getBtnSetMap().setOnAction(e -> {
            try {
                setMapPanel();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menuView.getBtnFr().setOnAction(e -> {
            game.setBundle("fr", "fr");
            updateButtonTextDisplay();

        });
        menuView.getBtnEn().setOnAction(e -> {
            game.setBundle("en", "us");
            updateButtonTextDisplay();
        });
        menuView.btnExit().setOnAction(e -> System.exit(0));
    }

    private void updateButtonTextDisplay() {
        menuView.updateButtonTextDisplay();
        setMapView.updateButtonTextDisplay();
        settingView.updateText();

    }

    private void setAllRooms() throws IOException {
        game.createRooms(setMapView.getImportedFile());
        if (game.getPlayer() != null) {
            Map.Entry<String, Room> entry = game.getAllRooms().entrySet().iterator().next();
            Room randomRoom = entry.getValue();
            game.getPlayer().setCurrentRoom(randomRoom);
        }
        if (game.getAllRooms() == null || game.getAllRooms().size() < 1) {
            System.out.println("A problem occured. No rooms created.");
        }
    }

    private void setSettingScene() throws IOException {
        // Charges les cartes en fonctions du fichier select avant
        setAllRooms();
        settingView.updatePanel();
        primaryStage.setScene(settingScene);
    }

    private void initSettingsScene() {
        settingScene = new Scene(settingsRoot, mainScene.getWidth(), mainScene.getHeight());
        settingView = new SettingView(settingsRoot, primaryStage, mainScene, game);
        settingView.getPlayButton().setOnAction(e -> {
            // ici lancer le jeu avec la carte choisie, avec les bon settings, et le jeu
            //ICI SET PLAYER ET CHARACTER Et carte :)
            setGridSize("30", "30");
            initGameStage();
            launchGame(gameStage);
            gameStage.setScene(gameScene);
            primaryStage.close();
            gameStage.show();
        });
    }

    private void setMapPanel() throws IOException {
        primaryStage.setScene(sceneSetMapView);
    }

    private void initSetMapScene() throws IOException {
        setMapView = new SetMapView(scoresScrollPane, setMapPaneRoot);
        sceneSetMapView = new Scene(setMapPaneRoot, mainScene.getWidth(), mainScene.getHeight());
    }



    private void launchGame(Stage gameStage) {
        GameBoard gameBoardDef = new GameBoard(X_VALUE, Y_VALUE);
        MovingPlayer movingPlayer = new MovingPlayer(gameBoardDef.getWidth(), gameBoardDef.getHeight());

        HashMap<Item, ItemDraw> items;
        GameView gameView = new GameView(game);
        GameController controller = new GameController(movingPlayer, gameBoardDef, gameView, game);

        StackPane gameRoot = new StackPane();

        gameScene = new Scene(gameRoot, gameBoardDef.getWidth() * 20 + 400, gameBoardDef.getHeight() * 20);

        gameScene.setOnKeyPressed(e -> controller.handle(e));

        gameView.makeScene(controller.getExits(), gameScene, movingPlayer, controller.getItems());
        HBox layout = new HBox();
        StackPane gameBoard = new StackPane();
        gameBoard.getChildren().addAll(gameView.getGridCanvas());
        gameBoard.setPadding(new Insets(10));
        StackPane.setAlignment(gameView.getGridCanvas(), Pos.CENTER_RIGHT);

        VBox panel = new VBox();
        Label nameLabel = createLabel("Room name: ");
        Label descLabel = createLabel("Description: ");
        Label playerNameLabel = createLabel("Name: ");
        Label playerLabel = createLabel("Player items: ");
        Label roomItemLabel = createLabel("Item in the room: ");
        Label roomCharacterLabel = createLabel("Character in the room: ");
        panel.getChildren().addAll(playerNameLabel, gameView.getUserName(), nameLabel, gameView.getRoomNameLabel(), descLabel, gameView.getRoomDescLabel(), playerLabel, roomItemLabel, roomCharacterLabel);
        //panel.setAlignment(Pos.CENTER);

        panel.setPrefWidth(400);
        panel.setPadding(new Insets(10));

        layout.getChildren().addAll(panel, gameBoard);
        layout.setAlignment(Pos.CENTER_RIGHT);

        gameRoot.getChildren().addAll(layout);
    }

    private Label createLabel(String descRoom) {
        Label label = new Label(descRoom);
        label.setFont(new Font("Verdana", 20));
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    public void initGameStage() {
        gameStage = new Stage();
        gameStage.setTitle("Snake - game");
    }

    public static Stage getGameStage() {
        return gameStage;
    }

    public void setGridSize(String width, String height) {
        boolean validGridSize = width.matches("^\\d+$") && height.matches("^\\d+$") && Integer.parseInt(width) >= 10
                && Integer.parseInt(width) <= 100 && Integer.parseInt(height) >= 10 && Integer.parseInt(height) <= 100;

        if (validGridSize) {
            X_VALUE = Integer.parseInt(width);
            Y_VALUE = Integer.parseInt(height);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong Data");
            alert.setContentText("Fields must be numbers between 10 and 100");
            alert.showAndWait();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getMainScene() {
        return mainScene;
    }
}
