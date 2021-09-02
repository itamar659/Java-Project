package components.application;

import Model.EngineModel;
import components.Resources;
import components.centerScreen.CenterHolderController;
import components.rightPanel.RightPanelController;
import components.problemInfo.ProbInfoController;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class ApplicationController {

    private enum Modes{
        Light, Dark, Ugly
    }

    private Stage primaryStage;
    private final UIAdapter adapter;
    private final EngineModel theEngine;

    private Modes currentMode = Modes.Light;
    private boolean isLogoRunAnimation = false;
    private boolean animatedSupport = true;

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
    @FXML private Button btnMode;
    @FXML private javafx.scene.image.ImageView imgView;
    @FXML private Label pathLbl;
    @FXML private Button buttonAnimation;
    @FXML private StackPane stackPaneLeft;
    @FXML private StackPane stackPaneRight;
    @FXML private ScrollPane scrollPaneCenter;
    @FXML private ScrollPane parent;
    @FXML private Label labelLogo;


    @FXML
    private void labelLogo_onClick(MouseEvent event){
        if(isLogoRunAnimation || !animatedSupport){
            return;
        }
        isLogoRunAnimation = true;
        RotateTransition rt = new RotateTransition(Duration.seconds(1.5), labelLogo);
        rt.setByAngle(360);
        rt.setCycleCount(1);
        rt.setOnFinished(event1 -> isLogoRunAnimation = false);
        rt.play();
    }

    @FXML
    void buttonAnimation_OnClick(ActionEvent event) {
        animatedSupport = !animatedSupport;

        if(animatedSupport){
            buttonAnimation.setText("Disable Animations");
        }else{
            buttonAnimation.setText("Enable Animations");
        }
    }

    @FXML
    public void changeMode(ActionEvent event){
        switch (currentMode){
            case Light:
                setDarkMode();
                currentMode = Modes.Dark;
                break;
            case Dark:
                setUglyMode();
                currentMode = Modes.Ugly;
                break;
            case Ugly:
                setLightMode();
                currentMode = Modes.Light;
                break;
        }
    }

    private void setUglyMode() {
        parent.getStylesheets().clear();
        parent.getStylesheets().add("styles/UglyMode.css");
        Image image = new Image("resources/poop.png");
        imgView.setImage(image);
    }

    private void setLightMode() {
        parent.getStylesheets().clear();
        parent.getStylesheets().add("styles/LightMode.css");
        Image image = new Image("resources/light_mode.png");
        imgView.setImage(image);
    }

    private void setDarkMode() {
        parent.getStylesheets().clear();
        parent.getStylesheets().add("styles/DarkMode.css");
        Image image = new Image("resources/dark_mode.png");
        imgView.setImage(image);
    }

    @FXML
    private void initialize() {
        pathLbl.textProperty().bind(selectedFileProperty);
        buttonOpenFile.disableProperty().bind(theEngine.isWorkingProperty().or(theEngine.isPausedProperty()));
        selectedFileProperty.addListener((observable, oldValue, newValue) -> {
            if (!(selectedFileProperty.get() != null && selectedFileProperty.get().equals(""))) {
                theEngine.isFileLoadedProperty().set(true);

                if(animatedSupport){
                    FadeTransition ft = new FadeTransition(Duration.seconds(2), pathLbl);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.setCycleCount(1);
                    ft.play();
                }
            }
        });

        loadProblemInfo();
        loadCenterHolder();
        loadRightPanel();

        adapter.getTheEngine().addEvoSettingsChangeListener(() ->
            probInfoController.updateEvoSettings(theEngine.getEvoEngineSettings())
        );
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
