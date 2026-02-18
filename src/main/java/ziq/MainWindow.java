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

    /**
     * Initializes the main window, consisting of images and dialog boxes.
     */
    @FXML
    public void initialize() {
        userImage = loadImage("/images/user.jpg");
        ziqImage = loadImage("/images/ziq.jpg");
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(
                "Hello, I'm Ziq!\nPlease give me a command!\nIf you need help, enter 'help.", ziqImage));
    }

    /** Sets up Ziq instance. */
    public void setZiq(Ziq z) {
        ziq = z;
    }

    /** Sets up the stage so the window can be closed on bye. */
    public void setStage(Stage s) {
        stage = s;
    }

    /**
     * Creates two dialog boxes, one for user input and the other for Ziq's reply,
     * Clears the user input after processing.
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
