package logic.Algorithm;

import logic.Algorithm.genericEvolutionAlgorithm.EvolutionAlgorithm;

public class TimeTableEvolutionAlgorithm extends EvolutionAlgorithm {

    @Override
    public void runAlgorithm(int generations) {
        // Step 1 - initialize the population
        population = new TimeTablePopulation(populationSize, problem);
        super.runAlgorithm(generations);
    }
}
