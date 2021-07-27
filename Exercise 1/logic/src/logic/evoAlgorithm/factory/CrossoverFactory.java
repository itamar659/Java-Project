package logic.evoAlgorithm.factory;

import logic.evoAlgorithm.crossovers.DayTimeOriented;
import engine.base.Crossover;
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
