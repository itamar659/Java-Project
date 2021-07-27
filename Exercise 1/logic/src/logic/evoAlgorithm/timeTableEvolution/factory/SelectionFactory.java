package logic.evoAlgorithm.timeTableEvolution.factory;

import logic.evoAlgorithm.base.Selection;
import logic.evoAlgorithm.timeTableEvolution.selections.Truncation;
import logic.timeTable.TimeTable;

public class SelectionFactory {

    public Selection<TimeTable> create(String selectionName) {
        if (selectionName == null) {
            return null;
        }

        if (selectionName.equals(Truncation.class.getSimpleName())) {
            return new Truncation();
        }

        return null;
    }
}
