package components.centerScreen;

import components.application.UIAdapter;
import components.Resources;
import components.centerScreen.timeTable.TimeTablePanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class CenterHolderController {

    @FXML
    private ProgressBar progressBarMaxGenerations;

    @FXML
    private Label labelMaxGenerationsPercentage;

    @FXML
    private ProgressBar progressBarMaxFitness;

    @FXML
    private ProgressBar progressBarMaxTime;

    @FXML
    private Label labelFitnessPercentage;

    @FXML
    private Label labelMaxTimePercentage;

    @FXML
    private CheckBox checkBoxMaxGenerations;

    @FXML
    private CheckBox checkBoxMaxFitness;

    @FXML
    private CheckBox checkBoxMaxTime;

    @FXML
    private StackPane stackPaneCenter;

    private UIAdapter uiAdapter;

    private TimeTablePanelController timeTablePanelController;

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Resources.TIME_TABLE_PANEL_FXML_RESOURCE);

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
        this.timeTablePanelController.timeTableSolutionProperty().bind(uiAdapter.getTheEngine().bestSolutionProperty());
        this.timeTablePanelController.setUiAdapter(uiAdapter);
    }
}
