package components.centerScreen;

import components.application.UIAdapter;
import components.Resources;
import components.centerScreen.timeTable.TimeTablePanelController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import logic.Engine;

import java.io.IOException;

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
    private TextField textFieldMaxGenerations;

    @FXML
    private TextField textFieldMaxFitness;

    @FXML
    private TextField textFieldMaxTime;

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

        checkBoxMaxGenerations.disableProperty().bind(uiAdapter.getTheEngine().isWorkingProperty());
        checkBoxMaxFitness.disableProperty().bind(checkBoxMaxGenerations.disableProperty());
        checkBoxMaxTime.disableProperty().bind(checkBoxMaxGenerations.disableProperty());

        bindCheckBoxes();
        bindTextFields();
        bindProgress();
    }

    private void bindCheckBoxes() {
        checkBoxMaxGenerations.selectedProperty().addListener((observable, oldValue, newValue) ->
                uiAdapter.getTheEngine().enableDisableStopCondition(Engine.StopCondition.MAX_GENERATIONS, newValue)
        );
        checkBoxMaxFitness.selectedProperty().addListener((observable, oldValue, newValue) ->
                uiAdapter.getTheEngine().enableDisableStopCondition(Engine.StopCondition.REQUESTED_FITNESS, newValue)
        );
        checkBoxMaxTime.selectedProperty().addListener((observable, oldValue, newValue) ->
                uiAdapter.getTheEngine().enableDisableStopCondition(Engine.StopCondition.BY_TIME, newValue)
        );
    }

    private void bindTextFields() {
        textFieldMaxGenerations.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                uiAdapter.getTheEngine().maxGenerationsConditionProperty().set(Integer.parseInt(newValue));
            } catch (Exception e) {
                uiAdapter.getTheEngine().maxGenerationsConditionProperty().set(100);
                textFieldMaxGenerations.setText(Integer.toString(100));
            }
        });
        textFieldMaxFitness.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                uiAdapter.getTheEngine().maxFitnessConditionProperty().set(Float.parseFloat(newValue));
            } catch (Exception e) {
                uiAdapter.getTheEngine().maxFitnessConditionProperty().set(80f);
                textFieldMaxFitness.setText(Integer.toString(80));
            }
        });
        textFieldMaxTime.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                uiAdapter.getTheEngine().timeConditionProperty().set(Integer.parseInt(newValue));
            } catch (Exception e) {
                uiAdapter.getTheEngine().timeConditionProperty().set(20);
                textFieldMaxTime.setText(Integer.toString(20));
            }
        });
    }

    private void bindProgress() {
        final String PERCENTAGE_FORMAT = "%.2f %%";
        labelMaxGenerationsPercentage.textProperty().bind(uiAdapter.getTheEngine().maxGenerationProgressProperty().multiply(100).asString(PERCENTAGE_FORMAT));
        labelFitnessPercentage.textProperty().bind(uiAdapter.getTheEngine().maxFitnessProgressProperty().multiply(100).asString(PERCENTAGE_FORMAT));
        labelMaxTimePercentage.textProperty().bind(uiAdapter.getTheEngine().timeProgressProperty().multiply(100).asString(PERCENTAGE_FORMAT));

        progressBarMaxGenerations.progressProperty().bind(uiAdapter.getTheEngine().maxGenerationProgressProperty());
        progressBarMaxFitness.progressProperty().bind(uiAdapter.getTheEngine().maxFitnessProgressProperty());
        progressBarMaxTime.progressProperty().bind(uiAdapter.getTheEngine().timeProgressProperty());
    }
}
