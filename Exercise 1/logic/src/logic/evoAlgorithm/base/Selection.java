package logic.evoAlgorithm.base;

import java.io.Serializable;

public interface Selection extends Serializable {

    Population select(Population population);
}
