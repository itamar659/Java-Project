package logic.evoAlgorithm.timeTableEvolution.factory;

import logic.evoAlgorithm.timeTableEvolution.crossovers.DayTimeOriented;
import logic.evoAlgorithm.base.Crossover;

public class CrossoverFactory {

    public Crossover create(String crossoverName) {
        if (crossoverName == null) {
            return null;
        }

        if (crossoverName.equals(DayTimeOriented.class.getSimpleName())) {
            return new DayTimeOriented();
        }

        return null;
    }
}
