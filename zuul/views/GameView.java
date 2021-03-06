package zuul.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import zuul.Game;
import zuul.Item;
import zuul.model.*;
import zuul.Character;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameView {
    private Canvas gridCanvas;
    private GraphicsContext gc;

    private Label roomNameLabel;
    private Label roomDescLabel;

    private Game game;
    private Label userName;
    private TableColumn itemDescPlayerCol = new TableColumn(Game.messages.getString("itemDescPlayerCol"));
    private TableColumn itemWeightPlayerCol = new TableColumn(Game.messages.getString("itemWeightPlayerCol"));
    private TableColumn itemDescRoomCol = new TableColumn(Game.messages.getString("itemDescRoomCol"));
    private TableColumn itemWeightRoomCol = new TableColumn(Game.messages.getString("itemWeightRoomCol"));
    private TableColumn charNameCol = new TableColumn(Game.messages.getString("charNameCol"));

    private Label nameLabel = createLabel(Game.messages.getString("nameLabel"));
    private Label descLabel = createLabel(Game.messages.getString("descLabel"));
    private Label playerNameLabel = createLabel(Game.messages.getString("playerNameLabel"));
    private Label playerLabel = createLabel(Game.messages.getString("playerLabel"));
    private Label roomItemLabel = createLabel(Game.messages.getString("roomItemLabel"));
    private Label roomCharacterLabel = createLabel(Game.messages.getString("roomCharacterLabel"));
    private Label exitsLabel  = createLabel(Game.messages.getString("exitsLabel"));
    private TableView<Item> tableItemPlayer = new TableView<Item>();
    private TableView<Item> tableItemRoom = new TableView<Item>();
    private TableView<Character> tableCharacters = new TableView<Character>();

    private Button dropButton = new Button(Game.messages.getString("dropButton"));
    private Button takeButton = new Button(Game.messages.getString("takeButton"));
    private Button giveButton = new Button(Game.messages.getString("giveButton"));

    private final ObservableList<Item> dataItemPlayer = FXCollections.observableArrayList();
    private final ObservableList<Item> dataItemRoom = FXCollections.observableArrayList();
    private final ObservableList<Character> dataCharacters = FXCollections.observableArrayList();

    private VBox vBoxItemPlayer = new VBox();
    private VBox vBoxItemRoom = new VBox();
    private final int scale = 20;
    private boolean updateinGameView = false;
    private VBox vBoxCharacter = new VBox();

    /**
     * check if we need to update game view
     * @return bool
     */
    public boolean isUpdateinGameView() {
        return updateinGameView;
    }

    /**
     * set update boolean (MVC)
     * @param updateinGameView bool
     */
    public void setUpdateinGameView(boolean updateinGameView) {
        this.updateinGameView = updateinGameView;
    }

    /**
     * get VBOX with item tab
     * @return
     */
    public VBox getvBoxItemPlayer() {
        return vBoxItemPlayer;
    }

    /**
     * get vbox with character tab
     * @return char
     */
    public VBox getvBoxCharacter() {
        return vBoxCharacter;
    }

    /**
     * create all tabls
     */
    public void createTables() {
        updateDatas();
        vBoxItemPlayer = createItemPlayerTable();
        vBoxItemRoom = createItemRoomTable();
        vBoxCharacter = createCharacterTable();
    }


    /**
     * create all labels
     * @param descRoom room
     * @return labels
     */
    private Label createLabel(String descRoom) {
        Label label = new Label(descRoom);
        label.setFont(new Font("Verdana", 20));
        label.setStyle("-fx-font-weight: bold;");
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     *
     * @param game
     */
    public GameView(Game game) {
        this.game = game;
    }

    /**
     * updates all the data in the tab
     */
    public void updateDatas() {
        dataItemPlayer.clear();
        dataItemRoom.clear();
        dataCharacters.clear();
        Map<String, Item> itemRoom = this.game.getPlayer().getCurrentRoom().getItems();
        Iterator iterator = itemRoom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Item tmp = (Item) me2.getValue();
            dataItemRoom.add(tmp);
        }
        Map<String, Item> itemPlayer = this.game.getPlayer().getItems();
        iterator = itemPlayer.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Item tmp = (Item) me2.getValue();
            dataItemPlayer.add(tmp);
        }
        Map<String, Character> characterRoom = this.game.getPlayer().getCurrentRoom().getCharacters();
        iterator = characterRoom.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Character tmp = (Character) me2.getValue();
            dataCharacters.add(tmp);
        }
    }

    /**
     * create char tab
     * @return vnox containing it
     */
    private VBox createCharacterTable() {
        tableCharacters.setEditable(true);
        charNameCol.setMinWidth(100);
        charNameCol.setCellValueFactory(new PropertyValueFactory<Character, String>("name"));
        giveButton.setOnAction(ev -> {
            Character r = tableCharacters.getSelectionModel().getSelectedItem();
            Item ra = tableItemPlayer.getSelectionModel().getSelectedItem();
            if (r != null && ra != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(Game.messages.getString("gave") + ra.getDescription() +" "+Game.messages.getString("to") + r.getName());
                alert.setHeaderText(Game.messages.getString("gave") + ra.getDescription() +" "+Game.messages.getString("to") + r.getName());
                alert.setContentText(Game.messages.getString("gave") + ra.getDescription() +" "+Game.messages.getString("to") + r.getName());
                alert.showAndWait();
                game.getPlayer().give(ra.getDescription(), r.getName());
                updateDatas();
            }
        });
        tableCharacters.setItems(dataCharacters);
        tableCharacters.getColumns().addAll(charNameCol);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tableCharacters, giveButton);
        return vbox;
    }

    /**
     * create item tab
     * @return vbox containing it
     */
    private VBox createItemRoomTable() {
        tableItemRoom.setEditable(true);
        itemDescRoomCol.setMinWidth(100);
        itemDescRoomCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        itemWeightRoomCol.setMinWidth(200);
        itemWeightRoomCol.setCellValueFactory(new PropertyValueFactory<Item, String>("weight"));
        takeButton.setOnAction(ev -> {
            Item r = tableItemRoom.getSelectionModel().getSelectedItem();
            if (r != null) {
                if (!game.getPlayer().take(r.getDescription())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(Game.messages.getString("alertTitleA"));
                    alert.setHeaderText(Game.messages.getString("alertHeader"));
                    alert.setContentText(Game.messages.getString("alertContent"));
                    alert.showAndWait();
                }
                updateDatas();
                updateinGameView = true;
            }
        });
        return getvBox(tableItemRoom, dataItemRoom, itemDescRoomCol, itemWeightRoomCol, takeButton);

    }

    /**
     * vbox
     * @param table
     * @param list
     * @param tableColumn
     * @param tableColumn1
     * @param button
     * @return
     */
    private VBox getvBox(TableView<Item> table, ObservableList<Item> list, TableColumn tableColumn, TableColumn tableColumn1, Button button) {
        table.setItems(list);
        table.getColumns().addAll(tableColumn, tableColumn1);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table, button);
        return vbox;
    }

    /**
     * create item player table
     * @return vbox
     */
    public VBox createItemPlayerTable() {
        tableItemPlayer.setEditable(true);
        itemDescPlayerCol.setMinWidth(100);
        itemDescPlayerCol.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        itemWeightPlayerCol.setMinWidth(200);
        itemWeightPlayerCol.setCellValueFactory(new PropertyValueFactory<Item, String>("weight"));
        dropButton.setOnAction(ev -> {
            Item r = tableItemPlayer.getSelectionModel().getSelectedItem();
            if (r != null) {
                game.getPlayer().drop(r.getDescription());
                updateDatas();
                updateinGameView = true;
            }
        });
        return getvBox(tableItemPlayer, dataItemPlayer, itemDescPlayerCol, itemWeightPlayerCol, dropButton);
    }

    /**
     * make scene
     * @param exits
     * @param scene
     * @param movingPlayer
     * @param items
     * @param characters
     */
    public void makeScene(HashMap<String, Exit> exits, Scene scene, MovingPlayer movingPlayer, HashMap<Item, ItemDraw> items, List<CharacterDraw> characters) {
        gridCanvas = new Canvas(scene.getWidth() - 400, scene.getHeight());
        gc = gridCanvas.getGraphicsContext2D();
        drawGrid(items, exits, movingPlayer, gc, characters);

        roomNameLabel = labelCreator(game.getPlayer().getCurrentRoom().getName());
        roomDescLabel = labelCreator(game.getPlayer().getCurrentRoom().getDescription());
        userName = labelCreator(game.getPlayer().getName());
    }

    /**
     * create labels
     * @param txt txt
     * @return label
     */
    private Label labelCreator(String txt) {
        Label label = new Label(txt);
        label.setFont(new Font("Verdana", 15));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * draw the grid
     * @param items
     * @param exits
     * @param movingPlayer
     * @param gc
     * @param characters
     */
    public void drawGrid(HashMap<Item, ItemDraw> items, HashMap<String, Exit> exits, MovingPlayer movingPlayer, GraphicsContext gc, List<CharacterDraw> characters) {
        gc.setFill(Color.web("#dedede"));
        gc.fillRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        drawExits(exits);
        drawItems(items);
        drawPlayer(movingPlayer);
        drawCharacters(characters);
    }

    /**
     * draw characters
     * @param characters
     */
    private void drawCharacters(List<CharacterDraw> characters) {
        for (CharacterDraw i : characters) {
            gc.drawImage(i.getImage(), i.getPos().x * scale, i.getPos().y * scale,
                    scale + 5, scale + 5);
        }

    }

    /**
     * draw exits
     * @param exits
     */
    private void drawExits(HashMap<String, Exit> exits) {
        Iterator iterator = exits.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            Exit item =  (Exit) me2.getValue();
            gc.drawImage(item.getImage(), item.getPos().x * scale, item.getPos().y * scale,
                    scale + 5, scale + 5);
        }
    }

    /**
     *
     * @param items
     */
    private void drawItems(HashMap <Item, ItemDraw> items) {
        Iterator iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            ItemDraw item = (ItemDraw) me2.getValue();
            gc.drawImage(item.getImage(), item.getPos().x * scale, item.getPos().y * scale,
                    scale + 5, scale + 5);
        }
    }

    /**
     * draw player
     * @param movingPlayer
     */
    private void drawPlayer(MovingPlayer movingPlayer) {
        gc.setFill(Color.web("#276324"));
        PlayerImg playerImg = new PlayerImg();
        gc.drawImage(playerImg.getImage(), movingPlayer.getCharPos().x * scale, movingPlayer.getCharPos().y * scale,
                scale +8, scale +8)
        ;

    }

    /**
     * update room name label
     * @param name
     */
    public void updateRoomNameLabel(String name) {
        roomNameLabel.setText(name);
    }

    /**
     * set text desc label
     * @param desc
     */
    public void roomDescLabel(String desc) {
        roomDescLabel.setText(desc);
    }

    /**
     * get canvas grid
     * @return
     */
    public Canvas getGridCanvas() {
        return gridCanvas;
    }

    /**
     * graphic context
     * @return
     */
    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    /**
     * get room label name
     * @return
     */
    public Label getRoomNameLabel() {
        return roomNameLabel;
    }

    /**
     * getter
     * @return
     */
    public Label getRoomDescLabel() {
        return roomDescLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getUserName() {
        return userName;
    }

    /**
     *getter
     * @return
     */
    public VBox getvBoxItemRoom() {
        return vBoxItemRoom;
    }

    /**
     *getter
     * @return
     */
    public Label getDescLabel() {
        return descLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getNameLabel() {
        return nameLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getPlayerNameLabel() {
        return playerNameLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getPlayerLabel() {
        return playerLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getRoomItemLabel() {
        return roomItemLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getRoomCharacterLabel() {
        return roomCharacterLabel;
    }

    /**
     *getter
     * @return
     */
    public Label getExitsLabel() {
        return exitsLabel;
    }
}