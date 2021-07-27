package engine.base;

import java.io.Serializable;

public abstract class Mutation<T> implements Serializable {

    protected double probability;

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public abstract void mutatePopulation(Population<T> population, Problem<T> problem);
}
