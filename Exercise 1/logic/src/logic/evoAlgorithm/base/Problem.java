package logic.evoAlgorithm.base;

import java.io.Serializable;

public interface Problem<T> extends Serializable {

    Solution<T> createSolution();
}
