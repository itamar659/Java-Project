package logic.evoAlgorithm.timeTableEvolution.crossovers;

import logic.evoAlgorithm.base.Crossover;
import logic.evoAlgorithm.base.Population;
import logic.timeTable.TimeTable;

public class AspectOriented implements Crossover<TimeTable> {

    public enum Orientation {
        CLASS, TEACHER;
    }

    private Orientation orientation;

    @Override
    public Population<TimeTable> crossover(Population<TimeTable> population, int reachSize) {
        return null;
    }
}
