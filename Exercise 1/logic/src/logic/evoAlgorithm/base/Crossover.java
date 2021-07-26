package logic.evoAlgorithm.base;

import java.io.Serializable;

public interface Crossover extends Serializable {

    Population crossover(Population population, int reachSize);
}
