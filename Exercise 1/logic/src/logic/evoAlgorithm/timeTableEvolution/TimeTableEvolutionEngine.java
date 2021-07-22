package logic.evoAlgorithm.timeTableEvolution;

import logic.evoAlgorithm.base.EvolutionEngine;

public class TimeTableEvolutionEngine extends EvolutionEngine {

    @Override
    public TimeTableProblem getProblem() {
        return (TimeTableProblem) super.getProblem();
    }

    @Override
    public void runAlgorithm(int generations) {
        // Step 1 - initialize the population
        population = new TimeTablePopulation(populationSize, problem);
        super.runAlgorithm(generations);
    }
}
