package logic.Algorithm.genericEvolutionAlgorithm.selections;

import logic.Algorithm.genericEvolutionAlgorithm.Selection;

import java.util.List;

public class SelectionFactory {

    private SelectionFactory() {
    }

    public static Selection createSelectionOperator(String selectionName, List<String> configuration) {
        if (selectionName.equals(Truncation.class.getSimpleName())) {
            // TODO: Check how to use configuration. (with "," between the string or whatever...)
            return new Truncation();
        }

        return null;
    }
}
