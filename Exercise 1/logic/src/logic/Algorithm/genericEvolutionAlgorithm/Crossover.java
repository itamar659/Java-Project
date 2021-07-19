package logic.Algorithm.genericEvolutionAlgorithm;

public interface Crossover {

    Population repopulateWithCrossover(Population population, int reachSize);
}
