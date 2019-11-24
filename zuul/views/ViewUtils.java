package zuul.views;

import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ViewUtils {
    /**
     * set root Pane size padding and bg
     * @param text text for the text file
     * @param size int for the text sier
     */

    static Text createTitles(String text, int size) {
        Color color = Color.WHITE;
        Font titleFont = Font.font("Verdana", size);
        Text title = new Text();
        title.setText(text);
        title.setFont(titleFont);
        title.setFill(color);
        return title;
    }

    public static BackgroundFill createBg() {
        return new BackgroundFill(Color.web("420d42"), CornerRadii.EMPTY, Insets.EMPTY);
    }
}
