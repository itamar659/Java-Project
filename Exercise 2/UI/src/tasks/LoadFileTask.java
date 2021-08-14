package tasks;

import javafx.concurrent.Task;
import logic.Engine;

import java.io.File;

public class LoadFileTask extends Task<Boolean> {

    private Engine theEngine;
    private File xmlFilePath;

    public LoadFileTask(Engine theEngine, File xmlFilePath) {
        this.theEngine = theEngine;
        this.xmlFilePath = xmlFilePath;
    }

    @Override
    protected Boolean call() throws Exception {
        theEngine.validateXMLFile(this.xmlFilePath);
        theEngine.updateEvoEngine();
        return true;
    }
}
