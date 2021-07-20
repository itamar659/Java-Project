package logic.Algorithm.factory;

import logic.Algorithm.genericEvolutionAlgorithm.Selection;
import logic.Algorithm.mutations.Flipping;
import logic.Algorithm.selections.Truncation;

public class SelectionFactory {

    private SelectionFactory() {
    }

    public static FactoryResult createSelection(String selectionName, String[][] configuration) {
        FactoryResult returnObject = new FactoryResult(true, String.format("Couldn't find the selection named: %s", selectionName));

        if (selectionName.equals(Truncation.class.getSimpleName())) {
            returnObject = Factory.setParameterizableParameters(new Truncation(), configuration);
        }

        return returnObject;
    }
}
