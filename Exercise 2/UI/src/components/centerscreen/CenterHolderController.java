package components.centerscreen;

import components.application.UIAdapter;
import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.timeTablePanel.TimeTablePanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class CenterHolderController {

    private UIAdapter uiAdapter;

    @FXML private Label labelPrecents;
    @FXML private ProgressBar progressBarTask;
    @FXML private StackPane stackPaneCenter;

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
    }
}
