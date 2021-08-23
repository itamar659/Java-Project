package engine.base;

import engine.configurable.Configurable;

import java.io.Serializable;

public interface Crossover<T> extends Serializable, Configurable {

    int getCuttingPoints();

    void setCuttingPoints(int cuttingPoints);

    Population<T> crossover(Population<T> population, int reachSize);
}
