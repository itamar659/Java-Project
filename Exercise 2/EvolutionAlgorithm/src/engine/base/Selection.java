package engine.base;

import java.io.Serializable;

public interface Selection<T> extends Serializable, HasName {

    Population<T> select(Population<T> population, int reduceSize);
}
