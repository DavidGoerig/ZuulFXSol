package zuul.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import zuul.Game;
import zuul.model.GameBoard;
import zuul.model.MovingPlayer;
import zuul.mygame.MyGame;
import zuul.room.Room;
import zuul.views.*;

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

    private static int X_VALUE;
    private static int Y_VALUE;


    // Jé fé ça
    private MyGame game = new zuul.mygame.MyGame("en", "us");
    private SetMapView setMapView = null;
    private SettingView settingView = null;
    private Scene sceneSetMapView;
    private Scene settingScene;
    private MenuView menuView;
    private GameView gameView;

    // TABS SHT

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
            setGridSize(30, 30);
            initGameStage();
            launchGame();
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



    private void launchGame() {
        GameBoard gameBoardDef = new GameBoard(X_VALUE, Y_VALUE);
        MovingPlayer movingPlayer = new MovingPlayer(gameBoardDef.getWidth(), gameBoardDef.getHeight());
        gameView = new GameView(game);
        GameController controller = new GameController(movingPlayer, gameBoardDef, gameView, game);

        StackPane gameRoot = new StackPane();

        gameScene = new Scene(gameRoot, gameBoardDef.getWidth() * 20 + 400, gameBoardDef.getHeight() * 20);
        gameScene.setOnKeyPressed(e -> controller.handle(e));

        gameView.makeScene(controller.getExits(), gameScene, movingPlayer, controller.getItems(), controller.getCharacters());
        HBox layout = new HBox();
        StackPane gameBoard = new StackPane();
        gameBoard.getChildren().addAll(gameView.getGridCanvas());
        gameBoard.setPadding(new Insets(10));
        StackPane.setAlignment(gameView.getGridCanvas(), Pos.CENTER_RIGHT);

        VBox panel = new VBox();
        panel.setBackground(new Background(ViewUtils.createBg()));
        panel.getChildren().addAll(gameView.getPlayerNameLabel(), gameView.getUserName(), gameView.getNameLabel(), gameView.getRoomNameLabel(), gameView.getDescLabel(),gameView.getRoomDescLabel(), gameView.getExitsLabel(), controller.getButtonMapBox(), gameView.getPlayerLabel(), gameView.getvBoxItemPlayer(), gameView.getRoomItemLabel(), gameView.getvBoxItemRoom() ,gameView.getRoomCharacterLabel(), gameView.getvBoxCharacter());


        panel.setPrefWidth(400);
        panel.setPadding(new Insets(10));

        layout.getChildren().addAll(panel, gameBoard);
        layout.setAlignment(Pos.CENTER_RIGHT);

        gameRoot.getChildren().addAll(layout);

        controller.updateButtonMapChanging();
        gameView.getGridCanvas().requestFocus();

    }

    public void initGameStage() {
        gameStage = new Stage();
        gameStage.setTitle("World of Zuul - David GOERIG");
    }

    public static Stage getGameStage() {
        return gameStage;
    }

    public void setGridSize(int width, int height) {
        X_VALUE = width;
        Y_VALUE = height;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getMainScene() {
        return mainScene;
    }
}
