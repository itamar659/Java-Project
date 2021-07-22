package logic.evoAlgorithm.base;

public abstract class Mutation {

    protected double probability;

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public abstract void mutatePopulation(Population population, Problem problem);
}
