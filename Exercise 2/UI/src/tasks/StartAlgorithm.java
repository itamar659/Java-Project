package tasks;

import Model.EngineModel;
import javafx.concurrent.Task;

public class StartAlgorithm extends Task<Boolean> {

    private final EngineModel theEngine;

    public StartAlgorithm(EngineModel theEngine) {
        this.theEngine = theEngine;
    }

    @Override
    protected Boolean call() throws Exception {
        this.theEngine.startAlgorithm();
        return true;
    }
}
