package logic.evoAlgorithm.factory;

import engine.base.Mutation;
import logic.evoAlgorithm.mutations.Flipping;
import logic.evoAlgorithm.mutations.Sizer;
import logic.timeTable.TimeTable;

public class MutationFactory {

    public Mutation<TimeTable> create(String mutationName) {
        if (mutationName == null) {
            return null;
        }

        if (mutationName.equals(Flipping.class.getSimpleName())) {
            return new Flipping();
        } else if (mutationName.equals(Sizer.class.getSimpleName())) {
            return new Sizer();
        }

        return null;
    }
}
