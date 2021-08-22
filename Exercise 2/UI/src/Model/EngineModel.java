package Model;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import logic.Engine;
import logic.evoEngineSettingsWrapper;
import logic.schema.exceptions.XMLExtractException;
import logic.timeTable.TimeTable;

import javax.xml.bind.JAXBException;
import java.io.File;

public class EngineModel {

    private final Engine theEngine;

    // Model Properties
    private final ObjectProperty<TimeTable> bestSolution = new SimpleObjectProperty<>();


    // Properties Getters
    public ObjectProperty<TimeTable> bestSolutionProperty() {
        return bestSolution;
    }

    public Engine getTheEngine() {
        return theEngine;
    }

    public EngineModel() {
        theEngine = new Engine();
        theEngine.addFinishRunListener( () -> Platform.runLater(this::onGenerationEnd));
        theEngine.addGenerationEndListener(() -> Platform.runLater(this::onGenerationEnd));
    }

    private void onGenerationEnd () {
        bestSolution.set(theEngine.getBestResult());
    }

    public void validateXMLFile(File xmlFilePath) throws JAXBException, XMLExtractException {
        theEngine.validateXMLFile(xmlFilePath);
    }

    public void updateEvoEngine() {
        theEngine.updateEvoEngine();
    }

    public void addGenerationEndListener(Runnable onGeneration) {
        theEngine.addGenerationEndListener(onGeneration);
    }

    public void addFinishRunListener(Runnable onFinish) {
        theEngine.addFinishRunListener(onFinish);
    }

    public void startAlgorithm() {
        theEngine.setStopCondition(Engine.StopCondition.MAX_GENERATIONS);
        theEngine.setMaxGenerationsCondition(1000);
        theEngine.setUpdateGenerationInterval(5);
        theEngine.startAlgorithm();
    }

    public void stopAlgorithm() {
        theEngine.stopAlgorithm();
        onGenerationEnd();
    }

    public evoEngineSettingsWrapper getEvoEngineSettings() {
        return theEngine.getEvoEngineSettings();
    }
}
