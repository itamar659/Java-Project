package Model;

import engine.Listeners;
import engine.base.Crossover;
import engine.base.Selection;
import engine.base.configurable.Configurable;
import javafx.application.Platform;
import javafx.beans.property.*;
import logic.Engine;
import logic.evoEngineSettingsWrapper;
import logic.schema.exceptions.XMLExtractException;
import logic.timeTable.TimeTable;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.InvalidObjectException;

public class EngineModel {

    private final Engine theEngine;
    private final Listeners evoSettingsChangeListeners;

    // Model Properties
    private final ObjectProperty<TimeTable> bestSolution = new SimpleObjectProperty<>();
    private final ObjectProperty<Crossover<TimeTable>> crossover = new SimpleObjectProperty<>();
    private final ObjectProperty<Selection<TimeTable>> selection = new SimpleObjectProperty<>();
    private final IntegerProperty elitism = new SimpleIntegerProperty(0);
    private final BooleanProperty isWorking = new SimpleBooleanProperty(false);
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isFileLoaded = new SimpleBooleanProperty(false);

    // TODO: 3 methods to update those properties for each stop condition.
    private final IntegerProperty MaxGenerationsCondition = new SimpleIntegerProperty(0);
    private final FloatProperty MaxFitnessCondition = new SimpleFloatProperty(0);
    private final FloatProperty TimeCondition = new SimpleFloatProperty(0);

    // Properties Getters
    public ObjectProperty<TimeTable> bestSolutionProperty() {
        return bestSolution;
    }

    public ObjectProperty<Crossover<TimeTable>> crossoverProperty() {
        return crossover;
    }

    public ObjectProperty<Selection<TimeTable>> selectionProperty() {
        return selection;
    }

    public IntegerProperty elitismProperty() {
        return elitism;
    }

    public BooleanProperty isWorkingProperty() {
        return isWorking;
    }

    public BooleanProperty isPausedProperty() {
        return isPaused;
    }

    public BooleanProperty isFileLoadedProperty() {
        return isFileLoaded;
    }

    // TODO: 3 methods to update those properties for each stop condition.
    public IntegerProperty maxGenerationsConditionProperty() {
        return MaxGenerationsCondition;
    }

    public FloatProperty maxFitnessConditionProperty() {
        return MaxFitnessCondition;
    }

    public FloatProperty timeConditionProperty() {
        return TimeCondition;
    }


    // Default Constructor
    public EngineModel() {
        theEngine = new Engine();
        theEngine.addFinishRunListener(this::onGenerationEnd);
        theEngine.addFinishRunListener(this::onFinish);
        theEngine.addGenerationEndListener(this::onGenerationEnd);
        evoSettingsChangeListeners = new Listeners();

        elitism.addListener((observable, oldValue, newValue) -> {
            theEngine.setElitism(newValue.intValue());
        });
    }

    public void addEvoSettingsChangeListener(Runnable func) {
        evoSettingsChangeListeners.add(func);
    }

    private void onGenerationEnd () {
        Platform.runLater(() -> bestSolution.set(theEngine.getBestResult()));
    }

    private void onFinish() {
        setIsWorking(false);
        setIsPaused(false);
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
        elitism.set(theEngine.getEvoEngineSettings().getElitism());
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
        theEngine.addStopCondition(Engine.StopCondition.MAX_GENERATIONS);
        theEngine.setMaxGenerationsCondition(100);
        theEngine.startAlgorithm();
    }

    public void stopAlgorithm() {
        theEngine.stopAlgorithm();
        onGenerationEnd();
        setIsWorking(false);
        setIsPaused(false);
    }

    public void resumeAlgorithm() {
        setIsWorking(true);
        setIsPaused(false);
        theEngine.resumeAlgorithm();
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

    public void ConfigObject(Object configurable, String paramName, String paramValue) {
        if (configurable instanceof Configurable) {
            ((Configurable)configurable).setParameter(paramName, paramValue);
            onEvoSettingsChange();
        } else {
            throw new IllegalArgumentException("Cannot config an instance that's not Configurable.");
        }
    }

    protected void onEvoSettingsChange() {
        Platform.runLater(evoSettingsChangeListeners::raiseEvent);
    }
}