package ziq;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Ziq ziq;
    private Stage stage;

    private Image userImage;
    private Image ziqImage;

    private static Image loadImage(String resourcePath) {
        var stream = MainWindow.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            return new WritableImage(100, 100);
        }
        try (stream) {
            return new Image(stream);
        } catch (Exception e) {
            return new WritableImage(100, 100);
        }
    }

    @FXML
    public void initialize() {
        userImage = loadImage("/images/user.jpg");
        ziqImage = loadImage("/images/ziq.jpg");
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(
                "Hello, I'm Ziq!\nWhat can I do for you?", ziqImage));
    }

    /** Injects the Ziq instance. */
    public void setZiq(Ziq z) {
        ziq = z;
    }

    /** Injects the Stage so the window can be closed on bye. */
    public void setStage(Stage s) {
        stage = s;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Ziq's reply,
     * and appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        userInput.clear();
        if (input.isEmpty()) {
            return;
        }
        String response = ziq.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, ziqImage)
        );
        if (stage != null && "Bye. Hope to see you again!".equals(response)) {
            stage.close();
        }
    }
}
