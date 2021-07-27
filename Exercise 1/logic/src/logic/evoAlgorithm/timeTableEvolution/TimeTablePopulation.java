package logic.evoAlgorithm.timeTableEvolution;

import logic.evoAlgorithm.base.Population;
import logic.evoAlgorithm.base.Problem;
import logic.timeTable.TimeTable;

public class TimeTablePopulation extends Population<TimeTable> {

    public TimeTablePopulation(int size, Problem<TimeTable> createNewPopulationWithProblem) {
        this.solutions = new TimeTable[size];

        if (createNewPopulationWithProblem != null) {
            for (int i = 0; i < size; i++) {
                this.solutions[i] = createNewPopulationWithProblem.createSolution();
            }
        }
    }

    @Override
    public Population<TimeTable> copySmallerPopulation(int size) {
        Population<TimeTable> newPopulation = new TimeTablePopulation(size, null);

        int copy = size;
        if (copy > this.getSize()) {
            copy = this.getSize();
        }

        for (int i = 0; i < copy; i++) {
            newPopulation.setSolutionByIndex(i, this.getSolutionByIndex(i));
        }

        return newPopulation;
    }

    @Override
    public Population<TimeTable> initializeSubPopulation(int size) {
        return new TimeTablePopulation(size, null);
    }
}
