package tasks;

import Model.EngineModel;
import javafx.concurrent.Task;

public class StartAlgorithm extends Task<Boolean> {

    private final EngineModel theEngine;
    private final boolean resumed;

    public StartAlgorithm(EngineModel theEngine, boolean resumed) {
        this.theEngine = theEngine;
        this.resumed = resumed;
    }

    @Override
    protected Boolean call() throws Exception {
        if (!resumed) {
            this.theEngine.startAlgorithm();
        } else {
            this.theEngine.resumeAlgorithm();
        }

        return true;
    }
}
