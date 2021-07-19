package logic.Algorithm.crossovers;

import logic.Algorithm.genericEvolutionAlgorithm.Crossover;

public class CrossoverFactory {

    private CrossoverFactory() {
    }

    public static Crossover createCrossover(String crossoverName) {
        if (crossoverName.equals(DayTimeOriented.class.getSimpleName())) {
            return new DayTimeOriented();
        }

        return null;
    }
}
