package components.rightPanel;

import components.Resources;
import components.application.UIAdapter;
import components.rightPanel.topRightPanel.TopRightController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class RightPanelController {

    @FXML
    private TextField textFieldInterval;

    @FXML
    private Button buttonStartPause;

    @FXML
    private Button buttonStop;

    @FXML
    private StackPane stackPaneTop;

    private UIAdapter uiAdapter;
    private TopRightController topRightController;

    private final BooleanProperty isPaused = new SimpleBooleanProperty(true);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        topRightController.setUiAdapter(uiAdapter);

        isRunning.bind(uiAdapter.getTheEngine().isWorkingProperty());
        isPaused.bind(uiAdapter.getTheEngine().isPausedProperty());
        textFieldInterval.disableProperty().bind(isPaused.or(isRunning));

        buttonStartPause.disableProperty().bind(
                (uiAdapter.getTheEngine().hasStopConditionProperty().and(
                        uiAdapter.getTheEngine().isFileLoadedProperty())).not());

        textFieldInterval.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                uiAdapter.getTheEngine().genIntervalProperty().set(Integer.parseInt(newValue));
            } catch (Exception e) {
                Platform.runLater(() -> textFieldInterval.setText("10"));
                uiAdapter.getTheEngine().genIntervalProperty().set(10);
            }
        });
    }

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Resources.TOP_RIGHT_PANEL_FXML_RESOURCE);

        try {
            Parent root = loader.load();
            this.topRightController = loader.getController();
            stackPaneTop.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isRunning.addListener((observable, oldValue, newValue) ->
                buttonStartPause.setText(isPaused.get() ? "Resume" : (!newValue ? "Start" : "Pause")));
        isPaused.addListener((observable, oldValue, newValue) ->
                buttonStartPause.setText(isRunning.get() ? "Pause" : (newValue ? "Resume" : "Start")));

        buttonStop.disableProperty().bind(buttonStartPause.disableProperty().or(isPaused.or(isRunning).not()));
    }

    @FXML
    void buttonStartPause_Clicked(ActionEvent event) {
        if (!isRunning.get() && !isPaused.get()) {
            uiAdapter.startAlgorithm();
        } else if (!isRunning.get() || isPaused.get()) {
            uiAdapter.resumeAlgorithm();
        } else {
            uiAdapter.pauseAlgorithm();
        }
    }

    @FXML
    void buttonStop_Clicked(ActionEvent event) {
        uiAdapter.stopAlgorithm();
    }
}