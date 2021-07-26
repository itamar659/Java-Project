package logic.evoAlgorithm.base;

import java.io.Serializable;

public abstract class Mutation implements Serializable {

    protected double probability;

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public abstract void mutatePopulation(Population population, Problem problem);
}
