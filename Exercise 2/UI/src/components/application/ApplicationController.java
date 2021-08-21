package components.application;

import components.centerscreen.CenterHolderController;
import components.problemInfo.ProbInfoController;
import components.timeTable.timeTablePanel.TimeTablePanelController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;

import java.io.File;
import java.io.IOException;

public class ApplicationController {

    private Stage primaryStage;
    private final UIAdapter adapter;
    private final Engine theEngine;

    private ProbInfoController probInfoController;
    private CenterHolderController centerHolderController;


    private final SimpleBooleanProperty isFileLoaded = new SimpleBooleanProperty();
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty();


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ApplicationController() {
        theEngine = new Engine();
        adapter = new UIAdapter(theEngine);
        probInfoController = new ProbInfoController();
        centerHolderController = new CenterHolderController();
    }

    @FXML
    private void initialize() {
        pathLbl.textProperty().bind(selectedFileProperty);
    }

    @FXML private Label pathLbl;
    @FXML private StackPane stackPaneLeft;
    @FXML private SplitPane stackPaneRight;
    @FXML private ScrollPane scrollPaneCenter;


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
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/components/problemInfo/ProbInfo.fxml"));
                    try {
                        Node node = loader.load();
                        probInfoController = loader.getController();
                        probInfoController.setProblem(theEngine.getEvoEngineSettings());
                        stackPaneLeft.getChildren().add(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FXMLLoader loader2 = new FXMLLoader();
                    loader2.setLocation(getClass().getResource("/components/centerscreen/centerHolder.fxml"));
                    try{
                        Node node1 = loader2.load();
                        centerHolderController = loader2.getController();
                        //stackPaneCenter.getChildren().add(node1);
                        scrollPaneCenter.setContent(node1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                },
                // On failed
                event -> {
                    event.getSource().getException().printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, event.getSource().getException().getMessage());
                    alert.showAndWait();
                });
    }

}
