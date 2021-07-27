package logic.evoAlgorithm.crossovers;

import engine.base.Crossover;
import engine.base.Population;
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
