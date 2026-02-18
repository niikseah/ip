package ziq;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Represents a dialog box with an accompanying image to represent speaker
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {

    private static final Set<String> VALID_COMMANDS = Set.of(
            "bye", "list", "mark", "unmark", "todo", "deadline", "event", "delete", "find", "schedule", "help");

    @FXML
    private VBox dialogWrapper;
    @FXML
    private TextFlow dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img, boolean isUser) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDialogContent(text, isUser);
        displayPicture.setImage(img);
        makeImageViewCircular(displayPicture);
    }

    /**
     * Sets HBox and VBox alignment: center when message height <= avatar height, top when taller.
     */
    private void updateAlignment() {
        double wrapperHeight = dialogWrapper.getHeight();
        double avatarHeight = displayPicture.getFitHeight();
        boolean useTop = wrapperHeight > avatarHeight + 1;
        dialogWrapper.setAlignment(useTop ? Pos.TOP_LEFT : Pos.CENTER_LEFT);
        boolean messageFirst = getChildren().get(0) == dialogWrapper;
        setAlignment(useTop
                ? (messageFirst ? Pos.TOP_RIGHT : Pos.TOP_LEFT)
                : (messageFirst ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT));
    }

    /**
     * Sets the dialog text. For user messages, colours the first word maroon if it is not a valid command.
     */
    private void setDialogContent(String text, boolean isUser) {
        dialog.getChildren().clear();
        if (text == null || text.isEmpty()) {
            return;
        }
        if (isUser) {
            String trimmed = text.trim();
            int space = trimmed.indexOf(' ');
            String firstWord = space < 0 ? trimmed : trimmed.substring(0, space);
            String rest = space < 0 ? "" : trimmed.substring(space);
            boolean validCommand = VALID_COMMANDS.contains(firstWord.toLowerCase());
            if (!validCommand && !firstWord.isEmpty()) {
                Text first = new Text(firstWord);
                first.setFill(Color.MAROON);
                Text restText = new Text(rest);
                restText.setFill(Color.BLACK);
                dialog.getChildren().addAll(first, restText);
            } else {
                Text full = new Text(text);
                full.setFill(Color.BLACK);
                dialog.getChildren().add(full);
            }
        } else {
            Text full = new Text(text);
            full.setFill(Color.WHITE);
            dialog.getChildren().add(full);
        }
    }

    /**
     * Clips the ImageView to a circle and scales the image to fill the circle (crop to circle).
     */
    private void makeImageViewCircular(ImageView imageView) {
        double size = 90;
        Image img = imageView.getImage();
        double fitW = size;
        double fitH = size;
        if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
            double w = img.getWidth();
            double h = img.getHeight();
            if (w >= h) {
                fitW = size * w / h;
                fitH = size;
            } else {
                fitW = size;
                fitH = size * h / w;
            }
            imageView.setFitWidth(fitW);
            imageView.setFitHeight(fitH);
        }
        double radius = Math.min(fitW, fitH) / 2;
        double centerX = fitW / 2;
        double centerY = fitH / 2;
        Circle clip = new Circle(centerX, centerY, radius);
        imageView.setClip(clip);
    }

    /**
     * Flips the dialog box for Ziq.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Applies Ziq's style (white text; blue bubble).
     */
    private void setZiqStyle() {
        dialogWrapper.setStyle("");
        getStyleClass().add("ziq-dialog");
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img, true);
    }

    public static DialogBox getDukeDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img, false);
        db.flip();
        db.setZiqStyle();
        return db;
    }
}
