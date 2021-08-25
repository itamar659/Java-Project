package components.timeTable.secondCenterScreen.components;

import components.application.UIAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import logic.evoAlgorithm.crossovers.SupportedCrossovers;

import java.util.Arrays;

public class CrossoverController {
    @FXML private ComboBox<String> comboBoxCrossover;

    private UIAdapter uiAdapter;

    @FXML
    private void initialize(){
        comboBoxCrossover.getItems().clear();

        Arrays.stream(SupportedCrossovers.values()).forEach(supportedCrossover ->
                comboBoxCrossover.getItems().add(supportedCrossover.getName())
        );
    }

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        uiAdapter.getTheEngine().crossoverProperty().addListener((observable, oldValue, newValue) -> {
            comboBoxCrossover.getSelectionModel().select(newValue.getName());
        });
    }

    @FXML
    private void comboBoxCrossover_SelectedItem(ActionEvent event){
        uiAdapter.getTheEngine().changeCrossover(comboBoxCrossover.getSelectionModel().getSelectedItem().replaceAll(" ", ""));
    }
}
