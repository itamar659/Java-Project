package Model;

import engine.Listeners;
import engine.base.Crossover;
import engine.base.Mutation;
import engine.base.Selection;
import engine.base.configurable.Configurable;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import logic.Engine;
import logic.evoEngineSettingsWrapper;
import logic.schema.exceptions.XMLExtractException;
import logic.timeTable.TimeTable;

import javax.xml.bind.JAXBException;
import java.io.File;

public class EngineModel {

    private final Engine theEngine;
    private final Listeners evoSettingsChangeListeners;

    // Model Properties
    private final ObjectProperty<TimeTable> bestSolution = new SimpleObjectProperty<>();
    private final ObjectProperty<Crossover<TimeTable>> crossover = new SimpleObjectProperty<>();
    private final ObjectProperty<Selection<TimeTable>> selection = new SimpleObjectProperty<>();
    private final ListProperty<Mutation<TimeTable>> mutations = new SimpleListProperty<>();
    private final IntegerProperty elitism = new SimpleIntegerProperty(0);
    private final BooleanProperty isWorking = new SimpleBooleanProperty(false);
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isFileLoaded = new SimpleBooleanProperty(false);

    private final IntegerProperty maxGenerationsCondition = new SimpleIntegerProperty(0);
    private final FloatProperty maxFitnessCondition = new SimpleFloatProperty(0);
    private final FloatProperty timeCondition = new SimpleFloatProperty(0);

    private final FloatProperty maxGenerationProgress = new SimpleFloatProperty(0);
    private final FloatProperty maxFitnessProgress = new SimpleFloatProperty(0);
    private final FloatProperty timeProgress = new SimpleFloatProperty(0);

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

    public ListProperty<Mutation<TimeTable>> mutationsProperty() {
        return mutations;
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

    public IntegerProperty maxGenerationsConditionProperty() {
        return maxGenerationsCondition;
    }

    public FloatProperty maxFitnessConditionProperty() {
        return maxFitnessCondition;
    }

    public FloatProperty timeConditionProperty() {
        return timeCondition;
    }

    // TODO: Be able to set the generation interval
    //  After that need to change onGenerationEnd listener to a new one that raise every generation and not gen interval.

    public FloatProperty maxGenerationProgressProperty() {
        return maxGenerationProgress;
    }

    public FloatProperty maxFitnessProgressProperty() {
        return maxFitnessProgress;
    }

    public FloatProperty timeProgressProperty() {
        return timeProgress;
    }

    // Default Constructor
    public EngineModel() {
        theEngine = new Engine();
        theEngine.addFinishRunListener(this::onGenerationEnd);
        theEngine.addFinishRunListener(this::onFinish);
        theEngine.addGenerationEndListener(this::onGenerationEnd);
        evoSettingsChangeListeners = new Listeners();

        elitism.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 100) {
                newValue = 100;
            } else if (newValue.intValue() < 0) {
                newValue = 0;
            }
            theEngine.setElitism(newValue.intValue());
            onEvoSettingsChange();
        });

        maxGenerationsCondition.addListener((observable, oldValue, newValue) ->
            theEngine.setMaxGenerationsCondition(newValue.intValue())
        );
        maxFitnessCondition.addListener((observable, oldValue, newValue) ->
                theEngine.setMaxFitnessCondition(newValue.floatValue())
        );
        timeCondition.addListener((observable, oldValue, newValue) ->
                theEngine.setTimeStopCondition(newValue.longValue())
        );
    }

    public void addEvoSettingsChangeListener(Runnable func) {
        evoSettingsChangeListeners.add(func);
    }

    private void onGenerationEnd() {
        Platform.runLater(() -> {
            // Update the best solution
            bestSolution.set(theEngine.getBestResult());
            // Update progress bars
            maxGenerationProgress.set(theEngine.getStopCondition(Engine.StopCondition.MAX_GENERATIONS).getProgress());
            maxFitnessProgress.set(theEngine.getStopCondition(Engine.StopCondition.REQUESTED_FITNESS).getProgress());
            timeProgress.set(theEngine.getStopCondition(Engine.StopCondition.BY_TIME).getProgress());
        });
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
        mutations.clear();
        mutations.set(FXCollections.observableArrayList(theEngine.getEvoEngineSettings().getMutations()));
        elitism.set(theEngine.getEvoEngineSettings().getElitism());
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

    public void enableDisableStopCondition(Engine.StopCondition stopCondition, boolean isEnabled) {
        if (isEnabled) {
            theEngine.addStopCondition(stopCondition);
        } else {
            theEngine.removeStopCondition(stopCondition);
        }
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