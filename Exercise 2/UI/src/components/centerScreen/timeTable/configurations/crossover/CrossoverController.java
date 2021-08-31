package components.centerScreen.timeTable.configurations.crossover;

import components.application.UIAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.evoAlgorithm.crossovers.SupportedCrossovers;

import java.util.Arrays;

public class CrossoverController {

    @FXML
    private ComboBox<String> comboBoxCrossover;

    @FXML
    private GridPane gridPaneConfig;

    private UIAdapter uiAdapter;

    @FXML
    private void initialize() {
        comboBoxCrossover.getItems().clear();

        Arrays.stream(SupportedCrossovers.values()).forEach(supportedCrossover ->
                comboBoxCrossover.getItems().add(supportedCrossover.getName())
        );
    }

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        uiAdapter.getTheEngine().crossoverProperty().addListener((observable, oldValue, newValue) -> {
            comboBoxCrossover.getSelectionModel().select(newValue.getName());
            createConfigurationsGrid();
        });
    }

    @FXML
    private void comboBoxCrossover_SelectedItem(ActionEvent event) {
        uiAdapter.getTheEngine().changeCrossover(comboBoxCrossover.getSelectionModel().getSelectedItem().replaceAll(" ", ""));
    }

    // TODO: Change the TextField to something else depends on the configuration type (i.e. combobox for enum)
    private void createConfigurationsGrid() {
        gridPaneConfig.getChildren().clear();
        gridPaneConfig.getRowConstraints().clear();

        final int[] rowIdx = {0};
        uiAdapter.getTheEngine().crossoverProperty().get().getConfiguration().getParameters().forEach((parameterName, parameterValue) -> {
            Label name = new Label(parameterName);
            TextField value = new TextField(parameterValue);
            value.textProperty().addListener((observable, oldValue, newValue) ->
                    updateValue(name, value)
            );

            gridPaneConfig.addRow(rowIdx[0], name, value);

            rowIdx[0]++;
        });
    }

    private void updateValue(Label paramName, TextField paramValue) {
        try {
            uiAdapter.getTheEngine().ConfigObject(uiAdapter.getTheEngine().crossoverProperty().get(), paramName.getText(), paramValue.getText());
            paramName.setStyle("-fx-text-fill: BLACK");
            paramValue.setStyle("-fx-text-fill: BLACK");
        } catch (Exception e) {
            paramName.setStyle("-fx-text-fill: RED");
            paramValue.setStyle("-fx-text-fill: RED");
        }
    }
}