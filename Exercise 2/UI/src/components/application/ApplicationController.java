package components.application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;

import java.io.File;

public class ApplicationController {

    private Stage primaryStage;
    private final UIAdapter adapter;

    private final SimpleBooleanProperty isFileLoaded = new SimpleBooleanProperty();
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty();


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ApplicationController() {
        adapter = new UIAdapter(new Engine());
    }

    @FXML
    private void initialize() {
        pathLbl.textProperty().bind(selectedFileProperty);
    }

    @FXML
    private Label pathLbl;

    @FXML
    private void openButtonClicked(ActionEvent event) {
        if (isFileLoaded.get()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "You'll erase your previous information. Are you sure you want to continue?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CLOSE ||
                    alert.getResult() == ButtonType.CANCEL) {
                return;
            }
        }

        openFileWithPopupWindow();
    }

    private void openFileWithPopupWindow() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        processFile(selectedFile.getAbsolutePath());
    }

    private void processFile(String path) {
        adapter.loadFile(
                // File path
                path,
                // On success
                event -> {
                    selectedFileProperty.set(path);
                    isFileLoaded.set(true);
                },
                // On failed
                event -> {
                    event.getSource().getException().printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, event.getSource().getException().getMessage());
                    alert.showAndWait();
                });
    }
}
