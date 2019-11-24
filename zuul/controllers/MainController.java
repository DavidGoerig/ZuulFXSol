package zuul.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import zuul.model.Item;
import zuul.model.Game;
import zuul.model.MovingPlayer;
import zuul.mygame.MyGame;
import zuul.room.Room;
import zuul.views.MenuView;
import zuul.views.GameView;
import zuul.views.SetMapView;
import zuul.views.SettingView;

import java.io.IOException;
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

    private char difficulty;
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
            setGridSize("25", "25");
            initGameStage();
            launchSnake(gameStage);
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



    private void launchSnake(Stage gameStage) {
        Game game = new Game(X_VALUE, Y_VALUE);
        MovingPlayer movingPlayer = new MovingPlayer(game.getWidth(), game.getHeight());
        Item item = new Item(game.getWidth(), game.getHeight(), movingPlayer);

        GameView gameView = new GameView(game);
        GameController controller = new GameController(movingPlayer, item, game, gameView);

        StackPane gameRoot = new StackPane();

        gameScene = new Scene(gameRoot, game.getWidth() * 20 + 200, game.getHeight() * 20);

        gameScene.setOnKeyPressed(e -> controller.handle(e));

        gameView.makeScene(controller.getExits(), gameScene, movingPlayer, item);
        HBox layout = new HBox();
        StackPane gameBoard = new StackPane();
        gameBoard.getChildren().addAll(gameView.getGridCanvas(), gameView.getGameOverLabel(), gameView.getUserName());
        gameBoard.setPadding(new Insets(10));
        StackPane.setAlignment(gameView.getGridCanvas(), Pos.CENTER_RIGHT);
        StackPane.setAlignment(gameView.getGameOverLabel(), Pos.TOP_CENTER);

        VBox panel = new VBox();
        panel.getChildren().addAll(gameView.getScoreLabel(), gameView.getTimerLabel());
        panel.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(panel, gameBoard);
        layout.setAlignment(Pos.CENTER_RIGHT);

        gameRoot.getChildren().addAll(layout);

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
