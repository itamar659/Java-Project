package engine.base;

import engine.base.configurable.Configurable;

import java.io.Serializable;

public interface Crossover<T> extends Serializable, HasName, Configurable {

    int getCuttingPoints();

    void setCuttingPoints(int cuttingPoints);

    Population<T> crossover(Population<T> population, int reachSize);
}
