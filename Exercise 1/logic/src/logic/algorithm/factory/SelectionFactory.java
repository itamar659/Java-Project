package logic.algorithm.factory;

import logic.algorithm.selections.Truncation;

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
