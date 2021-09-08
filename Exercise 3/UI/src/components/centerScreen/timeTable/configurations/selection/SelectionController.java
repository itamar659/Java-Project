package components.centerScreen.timeTable.configurations.selection;

import components.application.UIAdapter;
import engine.base.configurable.Configurable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.evoAlgorithm.selections.SupportedSelections;

import java.util.Arrays;

public class SelectionController {

    @FXML
    private ComboBox<String> comboBoxSelection;

    @FXML
    private GridPane gridPaneConfig;

    private UIAdapter uiAdapter;

    @FXML
    private void initialize() {
        comboBoxSelection.getItems().clear();

        Arrays.stream(SupportedSelections.values()).forEach(selection ->
                comboBoxSelection.getItems().add(selection.getName())
        );
    }

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        uiAdapter.getTheEngine().selectionProperty().addListener((observable, oldValue, newValue) -> {
            comboBoxSelection.getSelectionModel().select(newValue.getName());
            createConfigurationsGrid();
        });
    }

    @FXML
    void comboBoxSelection_ItemChanged(ActionEvent event) {
        uiAdapter.getTheEngine().changeSelection(comboBoxSelection.getSelectionModel().getSelectedItem().replaceAll(" ", ""));
    }

    // TODO: Change the TextField to something else depends on the configuration tpye (i.e. combobox for enum)
    private void createConfigurationsGrid() {
        gridPaneConfig.getChildren().clear();
        gridPaneConfig.getRowConstraints().clear();

        final int[] rowIdx = {0};
        if (uiAdapter.getTheEngine().selectionProperty().get() instanceof Configurable) {
            ((Configurable)uiAdapter.getTheEngine().selectionProperty().get()).getConfiguration().getParameters().forEach(
                    (parameterName, parameterValue) -> {
                Label name = new Label(parameterName);
                TextField value = new TextField(parameterValue);
                value.textProperty().addListener((observable, oldValue, newValue) ->
                        updateValue(name, value)
                    );

                gridPaneConfig.addRow(rowIdx[0], name, value);

                rowIdx[0]++;
            });
        }
    }

    private void updateValue(Label paramName, TextField paramValue) {
        try {
            uiAdapter.getTheEngine().ConfigObject(uiAdapter.getTheEngine().selectionProperty().get(), paramName.getText(), paramValue.getText());
            paramName.setStyle("");
            paramValue.setStyle("");
        } catch (Exception e) {
            paramName.setStyle("-fx-text-fill: RED");
            paramValue.setStyle("-fx-text-fill: RED");
        }
    }
}
