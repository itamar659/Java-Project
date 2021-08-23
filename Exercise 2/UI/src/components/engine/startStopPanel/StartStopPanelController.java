package components.engine.startStopPanel;

import components.application.UIAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartStopPanelController {

    private UIAdapter uiAdapter;

    private final BooleanProperty isPaused = new SimpleBooleanProperty(true);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;

        isRunning.bind(uiAdapter.getTheEngine().isWorkingProperty());
        isPaused.bind(uiAdapter.getTheEngine().isPausedProperty());
    }

    @FXML
    private Button buttonStartPause;

    @FXML
    private Button buttonStop;

    @FXML
    private void initialize() {
        isRunning.addListener((observable, oldValue, newValue) ->
                buttonStartPause.setText(!newValue ? "Start" : "Pause"));

        buttonStop.disableProperty().bind((isPaused.or(isRunning).not()));
    }

    @FXML
    void buttonStartPause_Clicked(ActionEvent event) {
        if (!isRunning.get() || isPaused.get()) {
            uiAdapter.startAlgorithm();
        } else {
            uiAdapter.pauseAlgorithm();
        }
    }

    @FXML
    void buttonStop_Clicked(ActionEvent event) {
        uiAdapter.stopAlgorithm();
    }

}
