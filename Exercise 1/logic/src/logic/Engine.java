package logic;

import logic.actions.Action;
import logic.evoAlgorithm.timeTableEvolution.TimeTableEvolutionEngine;
import logic.timeTable.TimeTable;
import logic.evoAlgorithm.base.*;
import logic.schema.TTEvoEngineCreator;
import logic.schema.XMLExtractException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Map;

// Wrapper with more functionality to EvolutionEngine
public class Engine {

    public enum StopCondition {
        MAX_GENERATIONS, REQUESTED_FITNESS;
    }

    public enum State {
        IDLE, RUNNING, COMPLETED;
    }

    private boolean isFileLoaded;
    private State state;
    private StopCondition stopCondition;

    private int stopConditionMaxGenerations;
    private float stopConditionMaxFitness;

    private final EvolutionEngine evoEngine;
    private final TTEvoEngineCreator TTEvoEngineCreator;
    private final evoEngineSettingsWrapper evoEngineSettings;

    public evoEngineSettingsWrapper getEvoEngineSettings() {
        return evoEngineSettings;
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    public State getState() {
        return state;
    }

    public void generationEndListener(Action action) {
        evoEngine.generationEndListener(action);
    }

    public void finishRunListener(Action action) {
        evoEngine.finishRunListener(action);
    }

    public  int getMaxGenerationsCondition() {
        return this.stopConditionMaxGenerations;
    }

    public void setMaxGenerationsCondition(int stopConditionMaxGenerations) {
        this.stopConditionMaxGenerations = stopConditionMaxGenerations;
    }

    public float getMaxFitnessCondition() {
        return stopConditionMaxFitness;
    }

    public void setMaxFitnessCondition(float stopConditionMaxFitness) {
        this.stopConditionMaxFitness = stopConditionMaxFitness;
    }

    public  int getCurrentGeneration() {
        return evoEngine.getCurrentGeneration();
    }

    public void setUpdateGenerationInterval(int everyGens) {
        evoEngine.setUpdateGenerationInterval(everyGens);
    }

    public TimeTable getBestResult() {
        return (TimeTable) evoEngine.getPopulation().getBestSolutionFitness();
    }

    public Map<Integer, Float> getHistoryGeneration2Fitness() {
        return evoEngine.getHistoryGeneration2Fitness();
    }

    public void setStopCondition(StopCondition stopCondition) {
        this.stopCondition = stopCondition;

        if (this.state != State.RUNNING) {
            switch (stopCondition) {
                case MAX_GENERATIONS:
                    evoEngine.setStopCondition(() -> evoEngine.getCurrentGeneration() >= this.stopConditionMaxGenerations);
                    break;
                case REQUESTED_FITNESS:
                    evoEngine.setStopCondition(() -> getBestResult().getFitness() >= stopConditionMaxFitness);
                    break;
            }
        }
    }

    public StopCondition getStopCondition() {
        return stopCondition;
    }

    public Engine() {
        this.state = State.IDLE;
        this.evoEngine = new TimeTableEvolutionEngine();
        this.evoEngineSettings = new evoEngineSettingsWrapper((TimeTableEvolutionEngine) this.evoEngine);
        this.TTEvoEngineCreator = new TTEvoEngineCreator();
    }

    public void validateXMLFile(File xmlFile) throws JAXBException, XMLExtractException {
        TTEvoEngineCreator.createFromXMLFile(xmlFile);
    }

    public void updateEvoEngine() {
        EvolutionEngine evolutionEngine = this.TTEvoEngineCreator.getLastCreatedTTEEngine();
        // Update the engine
        this.evoEngine.setPopulationSize(evolutionEngine.getPopulationSize());
        this.evoEngine.setSelection(evolutionEngine.getSelection());
        this.evoEngine.setCrossover(evolutionEngine.getCrossover());
        this.evoEngine.setMutations(evolutionEngine.getMutations());
        this.evoEngine.setProblem(evolutionEngine.getProblem());

        this.isFileLoaded = true;
        this.state = State.IDLE;
    }

    public void startAlgorithm() {
        this.state = State.RUNNING;
        // Different thread
        // In order to do it, maybe the evolutionEngine should have the state field.
        this.evoEngine.runAlgorithm();
        this.state = State.COMPLETED;
    }
}
