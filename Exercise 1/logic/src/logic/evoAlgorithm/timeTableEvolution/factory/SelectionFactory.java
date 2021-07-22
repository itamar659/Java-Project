package logic.evoAlgorithm.timeTableEvolution.factory;

import logic.evoAlgorithm.base.Selection;
import logic.evoAlgorithm.base.selections.Truncation;

public class SelectionFactory {

    public Selection create(String selectionName) {
        if (selectionName == null) {
            return null;
        }

        if (selectionName.equals(Truncation.class.getSimpleName())) {
            return new Truncation();
        }

        return null;
    }
}
