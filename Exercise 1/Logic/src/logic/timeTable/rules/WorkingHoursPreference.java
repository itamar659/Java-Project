package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class WorkingHoursPreference extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "WorkingHoursPreference";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        throw new NotImplementedException();
    }
}
