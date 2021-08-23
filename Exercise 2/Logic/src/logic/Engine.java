package logic;

import logic.evoAlgorithm.TimeTableEvolutionEngine;
import logic.timeTable.TimeTable;
import engine.base.*;
import logic.schema.TTEvoEngineCreator;
import logic.schema.exceptions.XMLExtractException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;

// Wrapper with more functionality to EvolutionEngine
public class Engine implements Serializable {

    public enum StopCondition {
        MAX_GENERATIONS, REQUESTED_FITNESS;
    }

    public enum State {
        IDLE, RUNNING, COMPLETED;
    }

    private boolean multiThreaded;
    private boolean isFileLoaded;
    private State state;
    private StopCondition stopCondition;

    private int stopConditionMaxGenerations;
    private float stopConditionMaxFitness;

    private final EvolutionEngine<TimeTable> evoEngine;
    private final TTEvoEngineCreator TTEvoEngineCreator;
    private final evoEngineSettingsWrapper evoEngineSettings;

    public evoEngineSettingsWrapper getEvoEngineSettings() {
        return evoEngineSettings;
    }

    public boolean isMultiThreaded() {
        return multiThreaded;
    }

    public void setMultiThreaded(boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    public State getState() {
        return state;
    }

    public void addGenerationEndListener(Runnable action) {
        evoEngine.addGenerationEndListener(action);
    }

    public void removeGenerationEndListener(Runnable action) {
        evoEngine.removeGenerationEndListener(action);
    }

    public void addFinishRunListener(Runnable action) {
        evoEngine.addFinishRunListener(action);
    }

    public void removeFinishRunListener(Runnable action) {
        evoEngine.removeFinishRunListener(action);
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
        return (TimeTable) evoEngine.getBestSolution();
    }

    public Map<Integer, Float> getHistoryGeneration2Fitness() {
        return evoEngine.getHistoryGeneration2Fitness();
    }

    public void setStopCondition(StopCondition stopCondition) {
        this.stopCondition = stopCondition;

        if (this.state != State.RUNNING) {
            switch (stopCondition) {
                case MAX_GENERATIONS:
                    evoEngine.setStopCondition((Supplier<Boolean> & Serializable)() -> evoEngine.getCurrentGeneration() >= this.stopConditionMaxGenerations);
                    break;
                case REQUESTED_FITNESS:
                    evoEngine.setStopCondition((Supplier<Boolean> & Serializable)() -> getBestResult().getFitness() >= stopConditionMaxFitness);
                    break;
            }
        }
    }

    public StopCondition getStopCondition() {
        return stopCondition;
    }

    public Engine() {
        this.multiThreaded = false;
        this.state = State.IDLE;
        this.stopCondition = StopCondition.MAX_GENERATIONS;
        this.evoEngine = new TimeTableEvolutionEngine();
        this.evoEngine.addFinishRunListener(this::algorithmFinished);
        this.evoEngineSettings = new evoEngineSettingsWrapper((TimeTableEvolutionEngine) this.evoEngine);
        this.TTEvoEngineCreator = new TTEvoEngineCreator();
    }

    public void validateXMLFile(File xmlFile) throws JAXBException, XMLExtractException {
        TTEvoEngineCreator.createFromXMLFile(xmlFile);
    }

    public void updateEvoEngine() {
        EvolutionEngine<TimeTable> evolutionEngine = this.TTEvoEngineCreator.getLastCreatedTTEEngine();
        if (evolutionEngine == null) {
            return;
        }

        // Update the engine
        this.evoEngine.clearHistory();
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
        this.evoEngine.runAlgorithm();
//        if (multiThreaded) {
//            new Thread(this.evoEngine::runAlgorithm, "Evolution Algorithm thread").start();
//        } else {
//            this.evoEngine.runAlgorithm();
//        }
    }

    public void stopAlgorithm() {
        this.evoEngine.stopAlgorithm();
    }

    public void pauseAlgorithm() {
        this.evoEngine.pauseAlgorithm();
    }

    private void algorithmFinished() {
        this.state = State.COMPLETED;
    }

    public void afterDeserialized() {
        this.evoEngine.addFinishRunListener(this::algorithmFinished);
    }
}
