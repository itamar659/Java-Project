package logic.Algorithm;

import logic.Algorithm.genericEvolutionAlgorithm.EvolutionAlgorithm;
import logic.Algorithm.genericEvolutionAlgorithm.Problem;

public class TimeTableEvolutionAlgorithm extends EvolutionAlgorithm {

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
