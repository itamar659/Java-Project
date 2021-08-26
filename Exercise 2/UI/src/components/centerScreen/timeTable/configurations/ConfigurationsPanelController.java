package components.centerScreen.timeTable.configurations;

import components.Resources;
import components.application.UIAdapter;
import components.centerScreen.timeTable.configurations.crossover.CrossoverController;
import components.centerScreen.timeTable.configurations.selection.SelectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class ConfigurationsPanelController {

    private CrossoverController crossoverController;
    private SelectionController selectionController;

    private UIAdapter uiAdapter;

    private Parent crossoverParent;
    private Parent selectionParent;

    @FXML
    private FlowPane flowPaneConfigurations;

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        crossoverController.setUiAdapter(uiAdapter);
        selectionController.setUiAdapter(uiAdapter);
    }

    @FXML
    private void initialize() {
        createCrossoverController();
        createSelectionController();

        flowPaneConfigurations.getChildren().add(crossoverParent);
        flowPaneConfigurations.getChildren().add(selectionParent);
    }

    private void createCrossoverController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.CROSSOVER_CONFIG_FXML_RESOURCE);
            crossoverParent = loader.load();
            crossoverController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Create controllers creator class that send back the controller and the parent in a "simple" complex object
    private void createSelectionController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.SELECTION_CONFIG_FXML_RESOURCE);
            selectionParent = loader.load();
            selectionController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
