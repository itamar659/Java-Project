package engine.base;

import java.io.Serializable;

public interface Crossover<T> extends Serializable {

    Population<T> crossover(Population<T> population, int reachSize);
}
