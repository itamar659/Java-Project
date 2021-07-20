package logic.Algorithm.genericEvolutionAlgorithm;

import logic.validation.Validateable;

public interface Selection extends Parameterizable, Validateable {

    Population select(Population population);
}
