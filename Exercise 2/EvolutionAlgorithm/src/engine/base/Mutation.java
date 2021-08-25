package engine.base;

import engine.base.configurable.Configurable;

import java.io.Serializable;

public interface Mutation<T> extends Serializable, HasName, Configurable {

    double getProbability();

    void setProbability(double probability);

    void mutatePopulation(Population<T> population, Problem<T> problem);
}
