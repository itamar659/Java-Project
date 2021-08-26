package Model;

import engine.Listeners;
import engine.base.Crossover;
import engine.base.Selection;
import javafx.application.Platform;
import javafx.beans.property.*;
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
    private final ObjectProperty<Crossover<TimeTable>> crossover = new SimpleObjectProperty<>();
    private final ObjectProperty<Selection<TimeTable>> selection = new SimpleObjectProperty<>();
    private final BooleanProperty isWorking = new SimpleBooleanProperty(false);
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isFileLoaded = new SimpleBooleanProperty(false);

    private final Listeners evoSettingsChangeListeners;

    public void addEvoSettingsChangeListener(Runnable func) {
        evoSettingsChangeListeners.add(func);
    }


    // Properties Getters
    public ObjectProperty<TimeTable> bestSolutionProperty() {
        return bestSolution;
    }

    public ObjectProperty<Crossover<TimeTable>> crossoverProperty() { return crossover; }

    public ObjectProperty<Selection<TimeTable>> selectionProperty() { return selection; }

    public BooleanProperty isWorkingProperty() {
        return isWorking;
    }

    public BooleanProperty isPausedProperty() {
        return isPaused;
    }

    public BooleanProperty isFileLoadedProperty() {
        return isFileLoaded;
    }

    // Default Constructor
    public EngineModel() {
        theEngine = new Engine();
        theEngine.addFinishRunListener( () -> Platform.runLater(this::onGenerationEnd));
        theEngine.addGenerationEndListener(() -> Platform.runLater(this::onGenerationEnd));
        evoSettingsChangeListeners = new Listeners();
    }

    private void onGenerationEnd () {
        Platform.runLater(() -> bestSolution.set(theEngine.getBestResult()));
    }

    public void validateXMLFile(File xmlFilePath) throws JAXBException, XMLExtractException {
        setIsWorking(true);
        try {
            theEngine.validateXMLFile(xmlFilePath);
        }
        finally {
            setIsWorking(false);
        }
    }

    public void updateEvoEngine() {
        setIsWorking(true);
        theEngine.updateEvoEngine();
        Platform.runLater(this::initializeAfterSerialize);
        setIsWorking(false);
    }

    private void initializeAfterSerialize() {
        bestSolution.set(null);
        crossover.set(theEngine.getEvoEngineSettings().getCrossover());
        selection.set(theEngine.getEvoEngineSettings().getSelection());
    }

    public void addGenerationEndListener(Runnable onGeneration) {
        theEngine.addGenerationEndListener(onGeneration);
    }

    public void addFinishRunListener(Runnable onFinish) {
        theEngine.addFinishRunListener(onFinish);
    }

    public void startAlgorithm() {
        setIsWorking(true);
        setIsPaused(false);
        theEngine.setUpdateGenerationInterval(5);
        theEngine.startAlgorithm();
    }

    public void stopAlgorithm() {
        theEngine.stopAlgorithm();
        onGenerationEnd();
        setIsWorking(false);
        setIsPaused(false);
    }

    public void pauseAlgorithm() {
        theEngine.pauseAlgorithm();
        onGenerationEnd();
        setIsWorking(false);
        setIsPaused(true);
    }

    public void setStopCondition(Engine.StopCondition stopCondition) {
        theEngine.addStopCondition(stopCondition);
    }

    public void setMaxGenerationsCondition(int maxGenerationsCondition) {
        theEngine.setMaxGenerationsCondition(maxGenerationsCondition);
    }

    public evoEngineSettingsWrapper getEvoEngineSettings() {
        return theEngine.getEvoEngineSettings();
    }

    private void setIsWorking(boolean isWorking) {
        Platform.runLater(() ->this.isWorking.set(isWorking));
    }

    private void setIsPaused(boolean isPaused) {
        Platform.runLater(() -> this.isPaused.set(isPaused));
    }

    public void changeCrossover(String crossoverName) {
        if (crossover.get() == null ||
                !crossover.get().getClass().getSimpleName().equals(crossoverName)) {
            theEngine.changeCrossover(crossoverName);
            crossover.set(theEngine.getEvoEngineSettings().getCrossover());
        }
        onEvoSettingsChange();
    }

    public void changeSelection(String selectionName) {
        if (selection.get() == null ||
                !selection.get().getClass().getSimpleName().equals(selectionName)) {
            theEngine.changeSelection(selectionName);
            selection.set(theEngine.getEvoEngineSettings().getSelection());
        }
        onEvoSettingsChange();
    }

    protected void onEvoSettingsChange() {
        Platform.runLater(evoSettingsChangeListeners::raiseEvent);
    }
}