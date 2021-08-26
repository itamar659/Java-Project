package components.centerScreen;

import components.application.UIAdapter;
import components.Resources;
import components.centerScreen.timeTable.TimeTablePanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

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
