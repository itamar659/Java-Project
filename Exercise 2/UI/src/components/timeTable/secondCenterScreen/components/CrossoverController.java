package components.timeTable.secondCenterScreen.components;

import components.application.UIAdapter;
import engine.base.Crossover;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import logic.evoAlgorithm.crossovers.SupportedCrossovers;
import logic.timeTable.TimeTable;

import java.util.Arrays;
import java.util.function.Consumer;

public class CrossoverController {
    @FXML private ComboBox<String> comboBoxCrossover;

    private UIAdapter uiAdapter;

    @FXML
    private void initialize(){
        comboBoxCrossover.getItems().clear();

        Arrays.stream(SupportedCrossovers.values()).forEach(supportedCrossovers -> comboBoxCrossover.getItems().add(supportedCrossovers.name()));
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
