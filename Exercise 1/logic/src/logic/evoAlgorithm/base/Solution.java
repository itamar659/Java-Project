package logic.evoAlgorithm.base;

import java.io.Serializable;

public interface Solution<T> extends Serializable {

    float getFitness();

    T getGens();
}
