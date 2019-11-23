package zuul.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import zuul.Game;
import zuul.room.Room;
import zuul.room.RoomModifyier;

import java.util.*;

public class SettingView {
    private Button playButton = new Button("Play");
    private Button backBtn  = new Button(Game.messages.getString("backBtn"));
    private Button removeNoExitBtn = new Button(Game.messages.getString("removeNoExitBtn"));
    private Button removeAllRoomNoItemBtn = new Button(Game.messages.getString("removeAllRoomNoItemBtn"));
    private Button setPlayerName  = new Button(Game.messages.getString("setPlayerName"));
    private Button addCharacterBtn = new Button(Game.messages.getString("addCharacterBtn"));

    private Button addItemToRoomBtn = new Button(Game.messages.getString("addItemToRoomBtn"));
    private  Button removeRoomBtn = new Button(Game.messages.getString("removeRoomBtn"));
    private Text title = new Text();
    private Text titleMapPart = new Text();
    private Text titleSetPlayer = new Text();
    private Text titleSetCharacter = new Text();

    private TableColumn<Room, String> characterCol = new TableColumn<Room, String>(Game.messages.getString("characterCol"));
    private TableColumn<Room, String> itemCol = new TableColumn<Room, String>(Game.messages.getString("itemCol"));
    private TableColumn<Room, String> exitWestCol = new TableColumn<Room, String>(Game.messages.getString("exitWestCol"));
    private TableColumn<Room, String> exitEastCol = new TableColumn<Room, String>(Game.messages.getString("exitEastCol"));
    private TableColumn<Room, String> exitSoutCol = new TableColumn<Room, String>(Game.messages.getString("exitSoutCol"));
    private TableColumn<Room, String> exitNorthCol = new TableColumn<Room, String>(Game.messages.getString("exitNorthCol"));

    private TableColumn nameCol = new TableColumn("Name");
    private TableColumn descCol = new TableColumn("Description");

    private Game game;
    private TextField inputGridSizeX = new TextField();
    private TextField inputGridSizeY = new TextField();
    private TextField inputObjName = new TextField();
    private TextField inputObjWeight = new TextField();
    private TextField inputCharacterName = new TextField();
    private HashMap<String, CheckBox> toggleCheckBoxList = new HashMap<>();
    private HBox checkboxesContainer = new HBox();
    private TableView<Room> table = new TableView<Room>();
    private final ObservableList<Room> data = FXCollections.observableArrayList();

    public void updateText() {
        backBtn.setText(Game.messages.getString("backBtn"));
        removeNoExitBtn.setText(Game.messages.getString("removeNoExitBtn"));
        removeAllRoomNoItemBtn.setText(Game.messages.getString("removeAllRoomNoItemBtn"));
        setPlayerName.setText(Game.messages.getString("setPlayerName"));
        addItemToRoomBtn.setText(Game.messages.getString("addItemToRoomBtn"));
        addCharacterBtn.setText(Game.messages.getString("addCharacterBtn"));
        removeRoomBtn.setText(Game.messages.getString("removeRoomBtn"));
        characterCol.setText(Game.messages.getString("characterCol"));
        itemCol.setText(Game.messages.getString("itemCol"));
        exitWestCol.setText(Game.messages.getString("exitWestCol"));
        exitEastCol.setText(Game.messages.getString("exitEastCol"));
        exitNorthCol.setText(Game.messages.getString("exitNorthCol"));
        exitSoutCol.setText(Game.messages.getString("exitSoutCol"));
        title.setText(Game.messages.getString("title2"));
        titleMapPart.setText(Game.messages.getString("titleMapPart"));
        titleSetPlayer.setText(Game.messages.getString("titleSetPlayer"));
        titleSetCharacter.setText(Game.messages.getString("titleSetCharacter"));
        inputGridSizeX.setPromptText(Game.messages.getString("inputGridSizeX"));
        inputGridSizeY.setPromptText(Game.messages.getString("inputGridSizeY"));
        inputObjName.setPromptText(Game.messages.getString("inputObjName"));
        inputObjWeight.setPromptText(Game.messages.getString("inputObjWeight"));
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public SettingView(Pane root, Stage primaryStage, Scene mainScene, Game game) {
        RoomModifyier mod = new RoomModifyier();
        updateText();
        this.game = game;

        backBtn.setOnAction(ev -> primaryStage.setScene(mainScene));
        Button addItemBtn = new Button("Add an item in all room without exits:");
        addItemBtn.setOnAction(ev -> {
            int weight = 1;
            if (isNumeric(inputGridSizeY.getText())) {
                weight = Integer.parseInt(inputGridSizeY.getText());
            }
            HashMap<String, Room> allRooms = mod.newItemAllRoomWithoutExits(game.getAllRooms(), inputGridSizeX.getText(), weight);
            game.setAllRooms(allRooms);
            updatePanel();
        });
        removeNoExitBtn.setOnAction(ev -> {
            HashMap<String, Room> allRooms = mod.removeAllWithoutExits(game.getAllRooms());
            game.setAllRooms(allRooms);
            updatePanel();
        });
        removeAllRoomNoItemBtn.setOnAction(ev -> {
            HashMap<String, Room> allRooms = mod.removeAllWithoutItems(game.getAllRooms());
            game.setAllRooms(allRooms);
            updatePanel();
        });

        setPlayerName.setOnAction(ev -> {
            game.getPlayer().setName(inputCharacterName.getText());
        });

        VBox container = new VBox();
        HBox roomChangerContainer = new HBox();
        HBox deleteRoomContainer = new HBox();
        HBox setPlayerNameContainer = new HBox();

        container.setAlignment(Pos.CENTER);
        checkboxesContainer.setAlignment(Pos.CENTER);
        HBox inputsContainer = new HBox();
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


        title.setFont(Font.font("Verdana", 35));
        titleMapPart.setFont(Font.font("Verdana", 20));
        titleSetPlayer.setFont(Font.font("Verdana", 20));
        titleSetCharacter.setFont(Font.font("Verdana", 20));

        updateToggleCheckboxes();

        // A LA PLACE DE CA METTRE LE NOMBRE DE JOUEURS
        inputCharacterName.setPromptText(game.getPlayer().getName());
        inputGridSizeX.setPrefWidth(200);
        inputGridSizeY.setPrefWidth(200);

        // ICI C'EST POUR ADD UN ITEM
        final HBox hboxAddItem = new HBox();
        hboxAddItem.setSpacing(5);
        hboxAddItem.setAlignment(Pos.CENTER);
        inputObjName.setPrefWidth(200);
        inputObjWeight.setPrefWidth(200);
        addItemToRoomBtn.setOnAction(ev -> {
            int weight = 1;
            if (isNumeric(inputObjWeight.getText())) {
                weight = Integer.parseInt(inputObjWeight.getText());
            }
            Room r = table.getSelectionModel().getSelectedItem();
            if (r == null)
                return;
            game.getAllRooms().get(r.getName()).addItem(inputObjName.getText(), weight);
            updatePanel();
        });
        hboxAddItem.getChildren().addAll(addItemToRoomBtn, inputObjName, inputObjWeight);
        VBox vbox = createRommTable();
        HBox vboxAddChar = createCharacters();

        setPlayerNameContainer.getChildren().addAll(setPlayerName, inputCharacterName);
        roomChangerContainer.getChildren().addAll(addItemBtn, inputsContainer);
        deleteRoomContainer.getChildren().addAll(removeAllRoomNoItemBtn, removeNoExitBtn);
        inputsContainer.getChildren().addAll(inputGridSizeX, inputGridSizeY);
        container.getChildren().addAll(title, titleMapPart, vbox, hboxAddItem, roomChangerContainer, deleteRoomContainer, titleSetPlayer, setPlayerNameContainer, checkboxesContainer, titleSetCharacter, vboxAddChar, playButton, backBtn);
        root.getChildren().add(container);
    }

    private VBox createRommTable() {
        data.clear();
        HashMap<String, Room> allroom = this.game.getAllRooms();
        Iterator iterator = allroom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Room temp = (Room) me2.getValue();
            data.add(temp);
        }
        table.setEditable(true);

        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Room, String>("name"));
        descCol.setMinWidth(200);
        descCol.setCellValueFactory(
                new PropertyValueFactory<Room, String>("description"));

        exitNorthCol.setMinWidth(100);
        exitNorthCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                Room tmp = p.getValue();
                if (tmp.getExit("north") == null)
                    return new SimpleStringProperty("null");
                return new SimpleStringProperty(tmp.getExit("north").getName());
            }
        });
        exitSoutCol.setMinWidth(100);
        exitSoutCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                Room tmp = p.getValue();
                if (tmp.getExit("south") == null)
                    return new SimpleStringProperty("null");
                return new SimpleStringProperty(tmp.getExit("south").getName());
            }
        });
        exitEastCol.setMinWidth(100);
        exitEastCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                Room tmp = p.getValue();
                if (tmp.getExit("east") == null)
                    return new SimpleStringProperty("null");
                return new SimpleStringProperty(tmp.getExit("east").getName());
            }
        });
        exitWestCol.setMinWidth(100);
        exitWestCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                Room tmp = p.getValue();
                if (tmp.getExit("west") == null)
                    return new SimpleStringProperty("null");
                return new SimpleStringProperty(tmp.getExit("west").getName());
            }
        });
        itemCol.setMinWidth(200);
        itemCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                Room tmp = p.getValue();
                return new SimpleStringProperty(tmp.getTtemDetails());
            }
        });
        characterCol.setMinWidth(200);
        characterCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                Room tmp = p.getValue();
                return new SimpleStringProperty(tmp.getCharacterDetails());
            }
        });
        // FIN DU TABLEAU
        removeRoomBtn.setOnAction(ev -> {
            Room r = table.getSelectionModel().getSelectedItem();
            game.deleteRoom(r);
            updatePanel();
        });
        table.setItems(data);
        table.getColumns().addAll(nameCol, descCol, exitNorthCol, exitEastCol, exitSoutCol, exitWestCol, itemCol, characterCol);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table, removeRoomBtn);
        return vbox;
    }

    private HBox createCharacters() {
        TextField setCharacterName = new TextField();
        setCharacterName.setPrefWidth(200);
        setCharacterName.setPromptText("Cecilia");
        HBox vboxAddChar = new HBox();
        vboxAddChar.setAlignment(Pos.CENTER);
        addCharacterBtn.setOnAction(ev -> {
            Room r = table.getSelectionModel().getSelectedItem();
            if (r == null)
                return;
            String name = "Cecilia";
            if (setCharacterName.getText() != "")
                name = setCharacterName.getText();
            game.createCharacter(name, r);
            updatePanel();
        });
        vboxAddChar.getChildren().addAll(addCharacterBtn, setCharacterName);
        return vboxAddChar;
    }

    private void createToggles() {
        toggleCheckBoxList.clear();
        HashMap<String, Room> allroom = this.game.getAllRooms();
        Iterator iterator = allroom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Room room = (Room) me2.getValue();
            CheckBox checkBox = new CheckBox(room.getName());
            checkBox.setSelected(false);
            toggleCheckBoxList.put(room.getName(), checkBox);
        }
    }

    private void togglesInHbow() {
        toggleCheckBoxList.get(game.getPlayer().getCurrentRoom().getName()).setSelected(true);
        checkboxesContainer.getChildren().clear();
        Iterator it = toggleCheckBoxList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry me2 = (Map.Entry) it.next();
            CheckBox temp = (CheckBox) me2.getValue();
            temp.setOnAction(e -> {
                if (!temp.isSelected()) {
                    temp.setSelected(true);
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

    private void updateToggleCheckboxes() {
        if (game.getAllRooms().size() == 0) {
            return;
        }
        // put the player in the first room
        Map.Entry<String, Room> entry = game.getAllRooms().entrySet().iterator().next();
        Room randomRoom = entry.getValue();
        game.getPlayer().setCurrentRoom(randomRoom);
        //create the toggle check box
        createToggles();
        // put the box in the hbox
        togglesInHbow();

    }

    public void updatePanel() {
        updateToggleCheckboxes();
        data.clear();
        HashMap<String, Room> allroom = this.game.getAllRooms();
        Iterator iterator = allroom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Room temp = (Room) me2.getValue();
            data.add(temp);
        }

        inputCharacterName.setPromptText(game.getPlayer().getName());
    }

    public Button getPlayButton() {
        return playButton;
    }
}
