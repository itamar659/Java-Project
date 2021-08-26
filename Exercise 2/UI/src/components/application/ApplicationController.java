package components.application;

import Model.EngineModel;
import components.Resources;
import components.centerScreen.CenterHolderController;
import components.rightPanel.RightPanelController;
import components.problemInfo.ProbInfoController;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ApplicationController {

    private Stage primaryStage;
    private final UIAdapter adapter;
    private final EngineModel theEngine;

    private ProbInfoController probInfoController;
    private CenterHolderController centerHolderController;
    private RightPanelController rightPanelController;

    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ApplicationController() {
        theEngine = new EngineModel();
        adapter = new UIAdapter(theEngine, this);
    }

    @FXML private Button buttonOpenFile;
    @FXML private Label pathLbl;
    @FXML private StackPane stackPaneLeft;
    @FXML private StackPane stackPaneRight;
    @FXML private ScrollPane scrollPaneCenter;

    @FXML
    private void initialize() {
        pathLbl.textProperty().bind(selectedFileProperty);
        buttonOpenFile.disableProperty().bind(theEngine.isWorkingProperty());
        selectedFileProperty.addListener((observable, oldValue, newValue) -> {
            if (!(selectedFileProperty.get() != null && selectedFileProperty.get().equals(""))) {
                theEngine.isFileLoadedProperty().set(true);
            }

        });

        loadProblemInfo();
        loadCenterHolder();
        loadRightPanel();

        adapter.getTheEngine().crossoverProperty().addListener((observable, oldValue, newValue) -> {
            probInfoController.updateEvoSettings(theEngine.getEvoEngineSettings());
        });
    }

    @FXML
    private void buttonOpenFile_Clicked(ActionEvent event) {
        if (theEngine.isFileLoadedProperty().get()) {
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
                    loadProblemInfo();
                });
    }

    private void loadProblemInfo() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Resources.PROBLEM_INFO_FXML_RESOURCE);
        try {
            Node node = loader.load();
            probInfoController = loader.getController();
            probInfoController.setProblem(adapter.getTheEngine().getEvoEngineSettings());
            stackPaneLeft.getChildren().clear();
            stackPaneLeft.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCenterHolder() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Resources.CENTER_HOLDER_FXML_RESOURCE);
        try {
            Node node = loader.load();
            centerHolderController = loader.getController();
            centerHolderController.setUiAdapter(adapter);
            scrollPaneCenter.setContent(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRightPanel() { // TODO: This is temporary. Need to change start stop panel location
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Resources.RIGHT_PANEL_FXML_RESOURCE);
        try {
            Node node = loader.load();
            rightPanelController = loader.getController();
            rightPanelController.setUiAdapter(adapter);
            stackPaneRight.getChildren().clear();
            stackPaneRight.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alertMessageLoadNewFile(Task<Boolean> task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Importing file");

        task.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            alert.setAlertType(Alert.AlertType.ERROR);
        });

        alert.contentTextProperty().bind(task.messageProperty());
        alert.show();
    }
}
