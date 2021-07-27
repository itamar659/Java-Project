package logic.evoAlgorithm.timeTableEvolution.factory;

import logic.evoAlgorithm.timeTableEvolution.crossovers.DayTimeOriented;
import logic.evoAlgorithm.base.Crossover;
import logic.timeTable.TimeTable;

public class CrossoverFactory {

    public Crossover<TimeTable> create(String crossoverName) {
        if (crossoverName == null) {
            return null;
        }

        if (crossoverName.equals(DayTimeOriented.class.getSimpleName())) {
            return new DayTimeOriented();
        }

        return null;
    }
}
