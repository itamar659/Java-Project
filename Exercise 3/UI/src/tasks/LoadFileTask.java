package tasks;

import Model.EngineModel;
import javafx.concurrent.Task;
import logic.Engine;
import logic.schema.exceptions.XMLExtractException;

import javax.xml.bind.JAXBException;
import java.io.File;

public class LoadFileTask extends Task<Boolean> {

    private final EngineModel theEngine;
    private final File xmlFilePath;

    public LoadFileTask(EngineModel theEngine, File xmlFilePath) {
        this.theEngine = theEngine;
        this.xmlFilePath = xmlFilePath;
    }

    @Override
    protected Boolean call() throws Exception {
        updateMessage("Loading file...");

        try {
            theEngine.validateXMLFile(this.xmlFilePath);
            theEngine.updateEvoEngine();
        } catch (JAXBException | XMLExtractException e) {
            updateMessage(e.getMessage());
            throw e;
        }

        updateMessage("File loaded!");
        return true;
    }
}
