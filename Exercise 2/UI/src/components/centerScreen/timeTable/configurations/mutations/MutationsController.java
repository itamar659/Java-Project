package components.centerScreen.timeTable.configurations.mutations;

import components.application.UIAdapter;
import engine.base.Mutation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.timeTable.TimeTable;

public class MutationsController {

    private UIAdapter uiAdapter;

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;

        uiAdapter.getTheEngine().mutationsProperty().addListener((observable, oldValue, newValue) -> {
            comboBoxMutation.getItems().clear();
            comboBoxMutation.getItems().setAll(newValue);
        });
    }

    @FXML
    private ComboBox<Mutation<TimeTable>> comboBoxMutation;

    @FXML
    private GridPane gridPaneConfig;

    @FXML
    private void initialize() {
        comboBoxMutation.setCellFactory(lv -> new MutationCell());
        comboBoxMutation.setButtonCell(new MutationCell());
    }

    @FXML
    void comboBoxMutation_SelectedItem(ActionEvent event) {
        if (comboBoxMutation.getSelectionModel().getSelectedItem() != null) {
            createConfigurationsGrid();
        }
    }

    // TODO: Change the TextField to something else depends on the configuration tpye (i.e. combobox for enum)
    private void createConfigurationsGrid() {
        gridPaneConfig.getChildren().clear();
        gridPaneConfig.getRowConstraints().clear();

        final int[] rowIdx = {0};
        comboBoxMutation.getSelectionModel().getSelectedItem().getConfiguration().getParameters().forEach((parameterName, parameterValue) -> {
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
            uiAdapter.getTheEngine().ConfigObject(comboBoxMutation.getSelectionModel().getSelectedItem(), paramName.getText(), paramValue.getText());
            paramName.setStyle("");
            paramValue.setStyle("");
        } catch (Exception e) {
            paramName.setStyle("-fx-text-fill: RED");
            paramValue.setStyle("-fx-text-fill: RED");
        }
    }

    private static class MutationCell extends ListCell<Mutation<TimeTable>> {

        @Override
        protected void updateItem(Mutation<TimeTable> item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText("");
            } else {
                setText(String.format("%s", item.getName()));
            }
        }
    }
}
