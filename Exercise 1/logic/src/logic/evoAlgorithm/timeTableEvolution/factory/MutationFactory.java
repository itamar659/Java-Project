package logic.evoAlgorithm.timeTableEvolution.factory;

import logic.evoAlgorithm.base.Mutation;
import logic.evoAlgorithm.timeTableEvolution.mutations.Flipping;
import logic.evoAlgorithm.timeTableEvolution.mutations.Sizer;
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
