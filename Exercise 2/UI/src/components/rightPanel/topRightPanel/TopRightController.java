package components.rightPanel.topRightPanel;

import components.application.UIAdapter;
import engine.base.Solution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import logic.timeTable.TimeTable;

public class TopRightController {

    private UIAdapter uiAdapter;

    @FXML
    private GridPane gridPaneInformation;

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;

        uiAdapter.getTheEngine().bestSolutionProperty().addListener((observable, oldValue, newValue) -> {
            createGridInformation(newValue);
        });
    }

    private void createGridInformation(Solution<TimeTable> solution) {
        if (solution == null) {
            return;
        }
        gridPaneInformation.getChildren().clear();
        gridPaneInformation.getRowConstraints().clear();

        int[] rowIdx = {1};
        solution.getGens().getRules().getListOfRules().forEach(rule -> {
            Label ruleLabel = new Label(String.format("%s (%s)", rule.getId(), rule.getType().name()));
            Label score = new Label(String.format("%f", rule.calcFitness(solution)));
            gridPaneInformation.addRow(rowIdx[0], ruleLabel, score);
            rowIdx[0]++;
        });
    }
}
