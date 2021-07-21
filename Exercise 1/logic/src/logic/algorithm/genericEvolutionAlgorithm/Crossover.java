package logic.algorithm.genericEvolutionAlgorithm;

public interface Crossover {

    Population repopulateWithCrossover(Population population, int reachSize);
}
