package components.centerScreen.timeTable.configurations.selection;

import components.application.UIAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import logic.evoAlgorithm.selections.SupportedSelections;

import java.util.Arrays;

public class SelectionController {

    @FXML
    private ComboBox<String> comboBoxSelection;

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
        });
    }

    @FXML
    void comboBoxSelection_ItemChanged(ActionEvent event) {
        uiAdapter.getTheEngine().changeSelection(comboBoxSelection.getSelectionModel().getSelectedItem().replaceAll(" ", ""));
    }

}
