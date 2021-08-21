package components.centerscreen;

import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.timeTablePanel.TimeTablePanelController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.net.URL;

public class CenterHolderController {
    @FXML private Label labelPrecents;
    @FXML private ProgressBar progressBarTask;
    @FXML private StackPane stackPaneCenter;
    private TimeTablePanelController timeTablePanelController;

    @FXML
    private void initialize() {
        timeTablePanelController = new TimeTablePanelController();

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

}
