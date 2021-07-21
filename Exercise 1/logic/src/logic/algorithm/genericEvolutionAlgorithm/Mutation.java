package logic.algorithm.genericEvolutionAlgorithm;

import logic.validation.Validateable;

public interface Mutation extends Parameterizable, Validateable {

    void mutatePopulation(Population population, float mutateChance, Problem problem);
}
