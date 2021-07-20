package logic.Algorithm.factory;

import logic.Algorithm.crossovers.DayTimeOriented;
import logic.Algorithm.genericEvolutionAlgorithm.Crossover;
import logic.Algorithm.mutations.Flipping;

public class CrossoverFactory {

    private CrossoverFactory() {
    }

    public static FactoryResult createCrossover(String crossoverName, String[][] configuration) {
        FactoryResult returnObject = new FactoryResult(true, String.format("Couldn't find the crossover named: %s", crossoverName));

        if (crossoverName.equals(DayTimeOriented.class.getSimpleName())) {
            returnObject = new FactoryResult(new DayTimeOriented());
        }

        return returnObject;
    }
}
