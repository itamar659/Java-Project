package logic.evoAlgorithm.base;

import java.io.Serializable;

public interface Selection<T> extends Serializable {

    Population<T> select(Population<T> population);
}
