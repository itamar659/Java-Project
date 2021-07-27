package logic.evoAlgorithm.timeTableEvolution.crossovers;

import logic.evoAlgorithm.base.Crossover;
import logic.evoAlgorithm.base.Population;

public class AspectOriented implements Crossover {

    public enum Orientation {
        CLASS, TEACHER;
    }

    private Orientation orientation;

    @Override
    public Population crossover(Population population, int reachSize) {
        return null;
    }
}
