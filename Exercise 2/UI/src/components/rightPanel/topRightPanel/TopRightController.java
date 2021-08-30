package components.rightPanel.topRightPanel;

import components.application.UIAdapter;
import engine.base.Solution;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rules;

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

        Node saveGroupForTable = gridPaneInformation.getChildren().get(0);
        gridPaneInformation.getChildren().clear();
        gridPaneInformation.getChildren().add(saveGroupForTable);
        gridPaneInformation.getRowConstraints().clear();

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
}
