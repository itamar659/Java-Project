package components.centerscreen;

import components.application.UIAdapter;
import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.timeTablePanel.TimeTablePanelController;
import engine.base.Crossover;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import logic.timeTable.TimeTable;

import java.io.IOException;
import java.net.URL;

public class CenterHolderController {

    @FXML
    private ProgressBar progressBarTask1;

    @FXML
    private Label labelTask1Percentage;

    @FXML
    private ProgressBar progressBarTask2;

    @FXML
    private ProgressBar progressBarTask3;

    @FXML
    private Label labelTask2Percentage;

    @FXML
    private Label labelTask3Percentage;

    @FXML
    private StackPane stackPaneCenter;

    private UIAdapter uiAdapter;

    private TimeTablePanelController timeTablePanelController;

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader();
        URL url = LessonsInfoResourcesConsts.TIME_TABLE_PANEL_FXML_RESOURCE;
        loader.setLocation(url);

        try {
            Parent root = loader.load();
            this.timeTablePanelController = loader.getController();
            stackPaneCenter.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        timeTablePanelController.timeTableSolutionProperty().bind(uiAdapter.getTheEngine().bestSolutionProperty());
        this.timeTablePanelController.setUiAdapter(uiAdapter);
    }
}
