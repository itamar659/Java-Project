package logic.evoAlgorithm;

import engine.base.EvolutionEngine;
import logic.timeTable.TimeTable;

public class TimeTableEvolutionEngine extends EvolutionEngine<TimeTable> {

    @Override
    public TimeTableProblem getProblem() {
        return (TimeTableProblem) super.getProblem();
    }

    @Override
    public void runAlgorithm() {
        // Step 1 - initialize the population
        population = new TimeTablePopulation(populationSize, problem);
        super.runAlgorithm();
    }
}
