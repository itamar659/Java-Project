package logic.algorithm;

import logic.algorithm.genericEvolutionAlgorithm.Population;
import logic.algorithm.genericEvolutionAlgorithm.Problem;

public class TimeTablePopulation extends Population {

    public TimeTablePopulation(int size, Problem createNewPopulationWithProblem) {
        this.solutions = new TimeTableSolution[size];

        if (createNewPopulationWithProblem != null) {
            for (int i = 0; i < size; i++) {
                this.solutions[i] = createNewPopulationWithProblem.createSolution();
            }
        }
    }

    @Override
    public Population copySmallerPopulation(int size) {
        Population newPopulation = new TimeTablePopulation(size, null);

        int copy = size;
        if (copy > this.getSize()) {
            copy = this.getSize();
        }

        for (int i = 0; i < copy; i++) {
            newPopulation.setSolutionByIndex(i, this.getSolutionByIndex(i));
        }

        return newPopulation;
    }
}
