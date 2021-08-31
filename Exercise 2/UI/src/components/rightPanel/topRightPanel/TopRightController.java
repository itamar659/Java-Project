package components.rightPanel.topRightPanel;

import components.application.UIAdapter;
import engine.base.Solution;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rules;

import java.util.Map;

public class TopRightController {

    private UIAdapter uiAdapter;

    @FXML
    private ComboBox<Map.Entry<Integer, TimeTable>> comboBoxSolutionGenerations;

    @FXML
    private GridPane gridPaneInformation;

    @FXML
    private Button buttonPrevGen;

    @FXML
    private Button buttonNextGen;

    @FXML
    private void initialize() {
        comboBoxSolutionGenerations.setCellFactory(lv -> new SolutionCell());
        comboBoxSolutionGenerations.setButtonCell(new SolutionCell());

        comboBoxSolutionGenerations.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            buttonNextGen.setDisable(false);
            buttonPrevGen.setDisable(false);

            if (newValue.intValue() == uiAdapter.getTheEngine().historySolutionsProperty().size() - 1) {
                buttonNextGen.setDisable(true);
            }
            if (newValue.intValue() <= 0) {
                buttonPrevGen.setDisable(true);
            }

        });
    }

    @FXML
    void comboBoxSolutionGenerations_SelectedIndexChanged(ActionEvent event) {
        Map.Entry<Integer, TimeTable> selected = comboBoxSolutionGenerations.getSelectionModel().selectedItemProperty().get();
        if (selected != null) {
            uiAdapter.getTheEngine().changeDisplaySolution(selected.getValue());
        }
    }

    @FXML
    void buttonNextGen_Clicked(ActionEvent event) {
        comboBoxSolutionGenerations.getSelectionModel().select(comboBoxSolutionGenerations.getSelectionModel().getSelectedIndex() + 1);
    }

    @FXML
    void buttonPrevGen_Clicked(ActionEvent event) {
        comboBoxSolutionGenerations.getSelectionModel().select(comboBoxSolutionGenerations.getSelectionModel().getSelectedIndex() - 1);
    }

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;

        uiAdapter.getTheEngine().displaySolutionProperty().addListener((observable, oldValue, newValue) -> {
            createGridInformation(newValue);
        });

        uiAdapter.getTheEngine().historySolutionsProperty().addListener((observable, oldValue, newValue) -> {
            buttonNextGen.setDisable(true);
            buttonPrevGen.setDisable(true);
            comboBoxSolutionGenerations.getItems().clear();
            comboBoxSolutionGenerations.setItems(
                    FXCollections.observableArrayList(uiAdapter.getTheEngine().historySolutionsProperty().get().entrySet())
            );
        });

        comboBoxSolutionGenerations.disableProperty().bind(uiAdapter.getTheEngine().isWorkingProperty());
    }

    private void createGridInformation(Solution<TimeTable> solution) {
        Node saveGroupForTable = gridPaneInformation.getChildren().get(0);
        gridPaneInformation.getChildren().clear();
        gridPaneInformation.getChildren().add(saveGroupForTable);
        gridPaneInformation.getRowConstraints().clear();

        if (solution == null) {
            return;
        }

        Label fitnessLabel = new Label("Fitness");
        Label value = new Label(String.format("%.2f", solution.getFitness()));
        gridPaneInformation.addRow(0, fitnessLabel, value);

        int[] rowIdx = {1};
        solution.getGens().getRules().getListOfRules().forEach(rule -> {
            Label ruleLabel = new Label(String.format("%s (%s)", rule.getId(), rule.getType().name()));
            Label score = new Label(String.format("%.2f", rule.calcFitness(solution)));
            gridPaneInformation.addRow(rowIdx[0], ruleLabel, score);
            rowIdx[0]++;
        });

        addAvgToGrid(rowIdx[0]++, solution, Rules.RULE_TYPE.SOFT);
        addAvgToGrid(rowIdx[0]++, solution, Rules.RULE_TYPE.HARD);


        gridPaneInformation.getChildren().forEach(child -> {
            if (child instanceof Label) {
                Label label = (Label) child;
                label.setPadding(new Insets(10, 10, 10, 10));
            }
        });
    }

    private void addAvgToGrid(int rowIdx, Solution<TimeTable> solution, Rules.RULE_TYPE type) {
        gridPaneInformation.addRow(rowIdx,
                new Label(String.format("%s Rules Avg.", type.name())),
                new Label(String.format("%.2f", solution.getGens().getAvgFitness(type))));
    }

    private static class SolutionCell extends ListCell<Map.Entry<Integer, TimeTable>> {

        @Override
        protected void updateItem(Map.Entry<Integer, TimeTable> item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText("");
            } else {
                setText(String.format("Gen: %d - %.2f", item.getKey(), item.getValue().getFitness()));
            }
        }
    }
}
