package engine.base;

import engine.configurable.Configurable;

import java.io.Serializable;

public interface Selection<T> extends Serializable, Configurable {

    Population<T> select(Population<T> population, int reduceSize);
}
