package zuul.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import zuul.Game;
import zuul.controllers.MainController;
import zuul.roomcsv.RoomCsvChecker;

import java.io.*;

public class SetMapView {
  private File importedFile = new File("zuul/res/config_file/game1.csv");
  private Text fileExample;
  private Text checkingPb;

  private Text title;
  private Text rule;
  private Button buttonChooseMap;
  private Button backBtn;

  /**
   * file handler (get the file from user input)
   * @throws IOException
   */
  private void fileHandler() throws IOException {
    RoomCsvChecker check = new RoomCsvChecker();
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters ().add(new FileChooser.ExtensionFilter("Csv Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(MainController.primaryStage);
    if (selectedFile != null) {
      if (check.checkFile(selectedFile.toPath())) {
        importedFile = selectedFile;
        System.out.println(importedFile);
        fileExample.setText(fileToString(importedFile));

        checkingPb.setFill(Color.GREEN);
        checkingPb.setText(Game.messages.getString("correctImport"));
      }
      else {
        checkingPb.setFill(Color.RED);
        checkingPb.setText(check.getErrorMessage());
      }
    }
  }

  /**
   * file to string to parse it
   * @param file
   * @return
   * @throws IOException
   */
  private String fileToString(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    String fnl = "";
    while ((st = br.readLine()) != null) {
      fnl += st + "\n";
    }
    return fnl;
  }

  /**
   * set text error
   * @return
   */
  private Text errorText() {
    Text title = new Text();
    title.setText("");
    title.setFill(Color.RED);
    title.setFont(Font.font("Verdana", 14));
    return title;
  }

  /**
   * file to text to display it
   * @param file
   * @return
   * @throws IOException
   */
  private Text fileToText(File file) throws IOException {
    String fnl = fileToString(file);
    Text title = new Text();
    title.setText(fnl);
    title.setFill(Color.BLACK);
    title.setFont(Font.font("Verdana", 14));
    return title;
  }


  public SetMapView(ScrollPane sp, Pane root) throws IOException {
    /** Filechooser */
    buttonChooseMap = new Button(Game.messages.getString("buttonChooseMap"));
    buttonChooseMap.setOnAction(e -> {
      try {
        fileHandler();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });

    /**
     * Padding
     */
    root.setPadding(new Insets(30));

    /**
     * Back to menu
     */
    backBtn = new Button(Game.messages.getString("backButton"));
    backBtn.setOnAction(ev -> MainController.primaryStage.setScene(MainController.mainScene));

    /**
      Adapt to panel
     */
    title = ViewUtils.createTitles(Game.messages.getString("titleMapView"), 28);
    rule = ViewUtils.createTitles(Game.messages.getString("rule"), 14);

    /**
     * ScoresRoot
     */
    Pane scoresRoot = new StackPane();
    sp.setContent(scoresRoot);
    sp.setPannable(true);
    sp.setFitToWidth(true);


    BackgroundFill myBF = ViewUtils.createBg();
    root.setBackground(new Background(myBF));
    scoresRoot.setBackground(new Background(myBF));

    fileExample = this.fileToText(importedFile);
    checkingPb = this.errorText();

    sp.setContent(fileExample);
    scoresRoot.getChildren().add(rule);

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    sp.setBackground(new Background(ViewUtils.createBg()));
    vbox.getChildren().addAll(title, rule, checkingPb, buttonChooseMap, sp, backBtn);
    root.getChildren().addAll(vbox);
  }

  /**
   * get imported file
   * @return
   */
  public File getImportedFile() {
    return importedFile;
  }

  /**
   * update button text dipsplay
   */
  public void updateButtonTextDisplay() {
    title.setText(Game.messages.getString("titleMapView"));
    rule.setText(Game.messages.getString("rule"));
    buttonChooseMap.setText(Game.messages.getString("buttonChooseMap"));
    backBtn.setText(Game.messages.getString("backButton"));
  }
}