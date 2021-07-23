package logic.timeTable.rules;

import logic.timeTable.rules.base.Rule;
import logic.evoAlgorithm.base.Solution;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class WorkingHoursPreference extends Rule {

    @Override
    public String getId() {
        return "WorkingHoursPreference";
    }

    @Override
    public float calcFitness(Solution solution) {
        throw new NotImplementedException();
    }
}
