package logic.Algorithm.genericEvolutionAlgorithm;

public interface Mutation {

    void mutatePopulation(Population population, float mutateChance, Problem problem);
}
