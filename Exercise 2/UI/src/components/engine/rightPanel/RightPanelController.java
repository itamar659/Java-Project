package components.engine.rightPanel;

import components.MainApplication;
import components.application.UIAdapter;
import components.engine.rightPanel.topRightPanel.TopRightContorller;
import components.timeTable.LessonsInfoResourcesConsts;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class RightPanelController {
    @FXML private Button buttonStartPause;
    @FXML private Button buttonStop;
    @FXML private StackPane stackPaneTop;


    private UIAdapter uiAdapter;
    private TopRightContorller topRightContorller;

    private final BooleanProperty isPaused = new SimpleBooleanProperty(true);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);


    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;

        isRunning.bind(uiAdapter.getTheEngine().isWorkingProperty());
        isPaused.bind(uiAdapter.getTheEngine().isPausedProperty());
    }

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/components/engine/rightPanel/topRightPanel/TopRightPanel.fxml");
        loader.setLocation(url);

        try {
            Parent root = loader.load();
            this.topRightContorller = loader.getController();
            stackPaneTop.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isRunning.addListener((observable, oldValue, newValue) ->
                    buttonStartPause.setText(isPaused.get() ? "Resume" : (!newValue ? "Start" : "Pause")));
        isPaused.addListener((observable, oldValue, newValue) ->
                    buttonStartPause.setText(isRunning.get() ? "Pause" : (newValue ? "Resume" : "Start")));

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
