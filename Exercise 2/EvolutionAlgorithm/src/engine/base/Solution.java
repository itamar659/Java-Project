package engine.base;

import engine.configurable.Configurable;

import java.io.Serializable;

public interface Solution<T> extends Serializable {

    float getFitness();

    T getGens();

    Solution<T> createChild();
}
