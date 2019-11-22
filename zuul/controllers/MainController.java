package zuul.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import zuul.model.Food;
import zuul.model.Game;
import zuul.model.Snake;
import zuul.mygame.MyGame;
import zuul.views.MenuView;
import zuul.views.GameView;
import zuul.views.SetMapView;

import java.io.IOException;

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

    private CheckBox checkEasy;
    private CheckBox checkMedium;
    private CheckBox checkHard;

    private TextField inputGridSizeX;
    private TextField inputGridSizeY;

    private char difficulty;
    private static int X_VALUE;
    private static int Y_VALUE;

    private Button playButton;

    // Jé fé ça
    private MyGame game = new zuul.mygame.MyGame("en", "us");
    private SetMapView setMapView = null;
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
        initSettingsScene();

        initSetMapScene();
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

    }

    private void setSettingScene() throws IOException {
        // Charges les cartes en fonctions du fichier select avant
        game.createRooms(setMapView.getImportedFile());

        if (game.getAllRooms() == null || game.getAllRooms().size() < 1) {
            System.out.println("A problem occured. No rooms created.");
            return ;
        }
        primaryStage.setScene(settingScene);
    }

    private void initSettingsScene() {
        /*
         DANS LES SETTINGS NOM JOUER, MODIFIER LA CARTE, NBR DE JOUEUR (1, 2? 3)
         MAP DE DEPART CHAMPION

         POUR LANCER LE JEU (my game), passer tout ça en paramètre
         */

        // ici ajouter les boutons pour supprimer des cartes etc
        // choisir ou commence le personnage

        // ajouter aussi le nom du Personnage
        settingScene = new Scene(settingsRoot, mainScene.getWidth(), mainScene.getHeight());
        settingsLayout(settingsRoot);
        playButton.setOnAction(e -> {
            // ici lancer le jeu avec la carte choisie, avec les bon settings, et le jeu
            setDifficulty();
            setGridSize(inputGridSizeX.getText(), inputGridSizeY.getText());
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

    private void settingsLayout(Pane root) {
        // ICI AJOUTER NOM, SALLE DE DEPART, ET LES 3 BOUTTONS POUR CHANGER LES ROOMS QUI SONT DANS GAME LA

        Button backBtn = new Button("<- Back");
        backBtn.setOnAction(ev -> primaryStage.setScene(mainScene));
        VBox container = new VBox();
        HBox checkboxesContainer = new HBox();
        HBox inputsContainer = new HBox();

        container.setAlignment(Pos.CENTER);
        checkboxesContainer.setAlignment(Pos.CENTER);
        inputsContainer.setAlignment(Pos.CENTER);

        container.setSpacing(40);
        checkboxesContainer.setSpacing(20);
        inputsContainer.setSpacing(20);

        Text title = new Text();
        title.setText("Settings");
        title.setFont(Font.font("Comic Sans MS", 20));

        // PEUT ETRE POUR CHOISIR LE SKIN?
        checkEasy = new CheckBox("Easy");
        checkMedium = new CheckBox("Normal");
        checkHard = new CheckBox("Hard");

        // A LA PLACE DE CA METTRE LE NOMBRE DE JOUEURS
        inputGridSizeX = new TextField();
        inputGridSizeY = new TextField();

        toggleCheckBoxes(checkEasy, false);
        toggleCheckBoxes(checkMedium, true);
        toggleCheckBoxes(checkHard, false);

        inputGridSizeX.setPromptText("width (10-100)");
        inputGridSizeY.setPromptText("height (10-100)");

        playButton = new Button("Play");

        checkboxesContainer.getChildren().addAll(checkEasy, checkMedium, checkHard);
        inputsContainer.getChildren().addAll(inputGridSizeX, inputGridSizeY);
        container.getChildren().addAll(title, backBtn, checkboxesContainer, inputsContainer, playButton);
        root.getChildren().add(container);
    }

    private void launchSnake(Stage gameStage) {
        Game game = new Game(X_VALUE, Y_VALUE, difficulty);
        Snake snake = new Snake(game.getWidth(), game.getHeight());
        Food food = new Food(game.getWidth(), game.getHeight(), snake);

        GameView gameView = new GameView(game);
        GameController controller = new GameController(snake, food, game, gameView);

        StackPane gameRoot = new StackPane();
        gameScene = new Scene(gameRoot, game.getWidth() * game.getScale() + 200, game.getHeight() * game.getScale());

        gameScene.setOnKeyPressed(e -> controller.handle(e));

        gameView.makeScene(gameScene, snake, food);
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

    private void toggleCheckBoxes(CheckBox checkBox, boolean selected) {
        checkBox.setSelected(selected);
        checkBox.setOnAction(e -> {
            checkEasy.setSelected(e.getSource().equals(checkEasy));
            checkMedium.setSelected(e.getSource().equals(checkMedium));
            checkHard.setSelected(e.getSource().equals(checkHard));
        });
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

    private void setDifficulty() {
        // changer le path du skin du player?
        if (checkEasy.isSelected()) {
            difficulty = 'E';
        } else if (checkMedium.isSelected()) {
            difficulty = 'N';
        } else if (checkHard.isSelected()) {
            difficulty = 'H';
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getMainScene() {
        return mainScene;
    }
}
