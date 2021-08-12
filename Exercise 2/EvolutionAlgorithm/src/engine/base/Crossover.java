package engine.base;

import java.io.Serializable;

public interface Crossover<T> extends Serializable {

    int getCuttingPoints();

    void setCuttingPoints(int cuttingPoints);

    Population<T> crossover(Population<T> population, int reachSize);
}
