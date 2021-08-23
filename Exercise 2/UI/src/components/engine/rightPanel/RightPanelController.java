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
