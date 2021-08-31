package components.centerScreen.timeTable.configurations;

import components.Resources;
import components.application.UIAdapter;
import components.centerScreen.timeTable.configurations.crossover.CrossoverController;
import components.centerScreen.timeTable.configurations.mutations.MutationsController;
import components.centerScreen.timeTable.configurations.selection.SelectionController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class ConfigurationsPanelController {

    private CrossoverController crossoverController;
    private SelectionController selectionController;
    private MutationsController mutationsController;

    private UIAdapter uiAdapter;

    private Parent crossoverParent;
    private Parent selectionParent;
    private Parent mutationsParent;

    @FXML
    private TextField textFieldElitism;

    @FXML
    private FlowPane flowPaneConfigurations;

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        crossoverController.setUiAdapter(uiAdapter);
        selectionController.setUiAdapter(uiAdapter);
        mutationsController.setUiAdapter(uiAdapter);
        uiAdapter.getTheEngine().elitismProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != 0) {
                textFieldElitism.setText(newValue.toString());
            }
        });

        flowPaneConfigurations.visibleProperty().bind(uiAdapter.getTheEngine().isWorkingProperty().not());
    }

    @FXML
    private void initialize() {
        createCrossoverController();
        createSelectionController();
        createMutationsController();

        flowPaneConfigurations.getChildren().add(crossoverParent);
        flowPaneConfigurations.getChildren().add(selectionParent);
        flowPaneConfigurations.getChildren().add(mutationsParent);

        textFieldElitism.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                uiAdapter.getTheEngine().elitismProperty().set(Integer.parseInt(newValue));
            } catch (Exception e) {
                Platform.runLater(() -> textFieldElitism.setText("0"));
                uiAdapter.getTheEngine().elitismProperty().set(0);
            }
        });
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

    private void createMutationsController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.MUTATIONS_CONFIG_FXML_RESOURCE);
            mutationsParent = loader.load();
            mutationsController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
