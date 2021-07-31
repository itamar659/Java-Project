package logic.evoAlgorithm.factory;

import engine.base.Selection;
import logic.evoAlgorithm.selections.Truncation;
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
