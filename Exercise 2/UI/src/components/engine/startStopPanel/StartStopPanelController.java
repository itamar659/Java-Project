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
    }

    @FXML
    private Button buttonStartPause;

    @FXML
    private Button buttonStop;

    @FXML
    private void initialize() {
        isPaused.addListener((observable, oldValue, newValue) ->
                buttonStartPause.setText(newValue ? "Start" : "Pause"));

        buttonStop.disableProperty().bind(isRunning.not());
    }

    @FXML
    void buttonStartPause_Clicked(ActionEvent event) {
        isRunning.set(true);

        if (isPaused.get()) {
            uiAdapter.startAlgorithm();
        } else {
            uiAdapter.pauseAlgorithm();
        }

        isPaused.set(!isPaused.get());
    }

    @FXML
    void buttonStop_Clicked(ActionEvent event) {
        isRunning.set(false);

        uiAdapter.stopAlgorithm();
    }

}
