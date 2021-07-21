package logic.algorithm.factory;

import logic.algorithm.crossovers.DayTimeOriented;

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
