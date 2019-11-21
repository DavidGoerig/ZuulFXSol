package zuul.views;

import com.sun.glass.ui.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import zuul.controllers.MainController;
import zuul.roomcsv.RoomCsvChecker;
import zuul.roomcsv.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SetMapView {
  private File importedFile = new File("zuul/res/config_file/game1.csv");
  private Text fileExample;
  private Text checkingPb;

  private void fileHandler() throws IOException {
    RoomCsvChecker check = new RoomCsvChecker();
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(MainController.primaryStage);
    if (selectedFile != null) {
      if (check.checkFile(selectedFile.toPath())) {
        System.out.println("TA GROSSE PUTE DE MERE");
        importedFile = selectedFile;
        fileExample.setText(fileToString(importedFile));
        checkingPb.setText("File correctly imported!");
      }
      else {
        checkingPb.setText(check.getErrorMessage());
      }
    }
  }

  private String fileToString(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    String fnl = "";
    while ((st = br.readLine()) != null) {
      fnl += st + "\n";
    }
    return fnl;
  }

  private Text errorText() {
    Text title = new Text();
    title.setText("");
    title.setFill(Color.RED);
    title.setFont(Font.font("Verdana", 14));
    return title;
  }

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
    Button buttonChooseMap = new Button("Select File");
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
    Button backBtn = new Button("<- Back");
    backBtn.setOnAction(ev -> MainController.primaryStage.setScene(MainController.mainScene));

    /**
      Adapt to panel
     */
    Text title = ViewUtils.createTitles("Import your own game setup:", 28);
    Text rule = ViewUtils.createTitles("If you don't change it the default file will be used.\nEach line of these files is a comma-separated list of strings of the form (.csv):\n<room-id>, <room-descr>, <north>, <east>, <south>, <west>, [<item>,<weight>]*", 14);

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

  public File getImportedFile() {
    return importedFile;
  }

  public void setImportedFile(File importedFile) {
    this.importedFile = importedFile;
  }
}