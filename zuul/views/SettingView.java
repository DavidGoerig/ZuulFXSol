package zuul.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zuul.Game;
import zuul.room.Room;
import zuul.room.RoomModifyier;

import java.util.*;

public class SettingView {
    private Button playButton;

    private String departRoom = null;
    private Game game;
    private TextField inputGridSizeX = new TextField();
    private TextField inputGridSizeY = new TextField();

    private TextField inputCharacterName = new TextField();

    private HashMap<String, CheckBox> toggleCheckBoxList = new HashMap<>();


    private HBox inputsContainer = new HBox();
    private HBox checkboxesContainer = new HBox();


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public SettingView(Pane root, Stage primaryStage, Scene mainScene, Game game) {
        RoomModifyier mod = new RoomModifyier();
        this.game = game;
        // ICI AJOUTER NOM, SALLE DE DEPART, ET LES 3 BOUTTONS POUR CHANGER LES ROOMS QUI SONT DANS GAME LA
        Button backBtn = new Button("<- Back");
        backBtn.setOnAction(ev -> primaryStage.setScene(mainScene));

        Button addItemBtn = new Button("Add an item in all room without exits:");
        addItemBtn.setOnAction(ev -> {
            int weight = 1;
            if (isNumeric(inputGridSizeY.getText())) {
                weight = Integer.parseInt(inputGridSizeY.getText());
            }
            HashMap<String, Room> allRooms = mod.newItemAllRoomWithoutExits(game.getAllRooms(), inputGridSizeX.getText(), weight);
            game.setAllRooms(allRooms);

        });
        Button removeNoExitBtn = new Button("Remove all room without exit.");
        removeNoExitBtn.setOnAction(ev -> {
            HashMap<String, Room> allRooms = mod.removeAllWithoutExits(game.getAllRooms());
            game.setAllRooms(allRooms);
            updateToggleCheckboxes();
        });
        Button removeAllRoomNoItemBtn = new Button("Remove all room without item.");
        removeAllRoomNoItemBtn.setOnAction(ev -> {
            HashMap<String, Room> allRooms = mod.removeAllWithoutItems(game.getAllRooms());
            game.setAllRooms(allRooms);
            updateToggleCheckboxes();
        });


        Button setPlayerName  = new Button("Set player name:");
        setPlayerName.setOnAction(ev -> {
            game.getPlayer().setName(inputCharacterName.getText());
        });

        VBox container = new VBox();
        HBox roomChangerContainer = new HBox();
        HBox deleteRoomContainer = new HBox();
        HBox setPlayerNameContainer = new HBox();

        container.setAlignment(Pos.CENTER);
        checkboxesContainer.setAlignment(Pos.CENTER);
        inputsContainer.setAlignment(Pos.CENTER);
        roomChangerContainer.setAlignment(Pos.CENTER);
        deleteRoomContainer.setAlignment(Pos.CENTER);
        setPlayerNameContainer.setAlignment(Pos.CENTER);

        container.setSpacing(20);
        checkboxesContainer.setSpacing(20);
        inputsContainer.setSpacing(20);
        roomChangerContainer.setSpacing(20);
        deleteRoomContainer.setSpacing(20);
        setPlayerNameContainer.setSpacing(20);

        Text title = new Text();
        title.setText("Settings");
        title.setFont(Font.font("Verdana", 35));

        Text titleMapPart = new Text();
        titleMapPart.setText("Modify the map:");
        titleMapPart.setFont(Font.font("Verdana", 20));

        Text titleSetPlayer = new Text();
        titleSetPlayer.setText("Set up player (default: name=David, position=random):");
        titleSetPlayer.setFont(Font.font("Verdana", 20));

        Text titleSetCharacter = new Text();
        titleSetCharacter.setText("Add some characters:");
        titleSetCharacter.setFont(Font.font("Verdana", 20));

        // PEUT ETRE POUR CHOISIR LE SKIN?
        updateToggleCheckboxes();

        // A LA PLACE DE CA METTRE LE NOMBRE DE JOUEURS
        inputGridSizeX.setPromptText("Object name (default: obj)");
        inputGridSizeY.setPromptText("Object weight (default: 1)");
        inputCharacterName.setPromptText(game.getPlayer().getName());
        inputGridSizeX.setPrefWidth(200);
        inputGridSizeY.setPrefWidth(200);

        playButton = new Button("Play");

        setPlayerNameContainer.getChildren().addAll(setPlayerName, inputCharacterName);
        roomChangerContainer.getChildren().addAll(addItemBtn, inputsContainer);
        deleteRoomContainer.getChildren().addAll(removeAllRoomNoItemBtn, removeNoExitBtn);
        inputsContainer.getChildren().addAll(inputGridSizeX, inputGridSizeY);
        container.getChildren().addAll(title, titleMapPart, roomChangerContainer, deleteRoomContainer, titleSetPlayer, setPlayerNameContainer, checkboxesContainer, titleSetCharacter, playButton, backBtn);
        root.getChildren().add(container);
    }

    private void updateToggleCheckboxes() {
        // put the player in the first room
        Map.Entry<String, Room> entry = game.getAllRooms().entrySet().iterator().next();
        Room randomRoom = entry.getValue();
        game.getPlayer().setCurrentRoom(randomRoom);
        //create the toggle check box
        toggleCheckBoxList.clear();
        HashMap<String, Room> allroom = this.game.getAllRooms();
        Iterator iterator = allroom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
            Room room = (Room) me2.getValue();
            CheckBox checkBox = new CheckBox(room.getName());
            checkBox.setSelected(false);
            toggleCheckBoxList.put(room.getName(), checkBox);
        }

        // put the box in the hbox

        toggleCheckBoxList.get(game.getPlayer().getCurrentRoom().getName()).setSelected(true);
        checkboxesContainer.getChildren().clear();
        Iterator it = toggleCheckBoxList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry me2 = (Map.Entry) it.next();
            CheckBox temp = (CheckBox) me2.getValue();
            temp.setOnAction(e -> {
                System.out.println("OUI TA MER");
                if (!temp.isSelected()) {
                    temp.setSelected(true);
                    System.out.println("OUI");
                }
                else {
                    Iterator i = toggleCheckBoxList.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry) i.next();
                        CheckBox tmp = (CheckBox) me.getValue();
                        tmp.setSelected(false);
                    }
                    temp.setSelected(true);
                    game.getPlayer().setCurrentRoom(game.getAllRooms().get(temp.getText()));
                }
            });
            checkboxesContainer.getChildren().addAll(temp);
        }
    }

    public void updatePanel() {
        // bien mettre la room de départ choisie
        updateToggleCheckboxes();

        inputCharacterName.setPromptText(game.getPlayer().getName());
        System.out.println(game.getAllRooms());
    }

    public Button getPlayButton() {
        return playButton;
    }
}
