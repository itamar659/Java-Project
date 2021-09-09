package logic;

import engine.base.stopConditions.MaxGenerationsStopCondition;
import engine.base.stopConditions.MaxFitnessStopCondition;
import engine.base.stopConditions.TimeStopCondition;
import logic.evoAlgorithm.TimeTableEvolutionEngine;
import logic.evoAlgorithm.factory.Factories;
import logic.timeTable.TimeTable;
import engine.base.*;
import logic.schema.TTEvoEngineCreator;
import logic.schema.exceptions.XMLExtractException;

import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// Wrapper with more functionality to EvolutionEngine
public class Engine implements Serializable {

    public enum StopCondition {
        MAX_GENERATIONS, REQUESTED_FITNESS, BY_TIME;
    }

    public enum State {
        IDLE, RUNNING, COMPLETED;
    }


    private boolean multiThreaded;
    private boolean isFileLoaded;
    private State state;

    private final MaxGenerationsStopCondition<TimeTable> maxGenerationsStopCondition;
    private final MaxFitnessStopCondition<TimeTable> maxFitnessStopCondition;
    private final TimeStopCondition timeStopCondition;

    private EvolutionEngine<TimeTable> evoEngine;
    private final evoEngineSettingsWrapper evoEngineSettings;
    private final Factories factories;

    public evoEngineSettingsWrapper getEvoEngineSettings() {
        return evoEngineSettings;
    }

    // === Regular Getters ===
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

    // === Listeners ===
    public void addRequiredIntervalListener(Runnable action) {
        evoEngine.addRequiredIntervalListener(action);
    }

    public void addOnEveryGenerationEnd(Runnable action) {
        evoEngine.addOnEveryGenerationEnd(action);
    }

    public void removeRequiredIntervalListener(Runnable action) {
        evoEngine.removeRequiredIntervalListener(action);
    }

    public void addFinishRunListener(Runnable action) {
        evoEngine.addFinishRunListener(action);
    }

    public void removeFinishRunListener(Runnable action) {
        evoEngine.removeFinishRunListener(action);
    }

    // === Stop Conditions ===
    public  int getMaxGenerationsCondition() {
        return maxGenerationsStopCondition.getMaxGenerations();
    }

    public void setMaxGenerationsCondition(int stopConditionMaxGenerations) {
        maxGenerationsStopCondition.setMaxGenerations(stopConditionMaxGenerations);
    }

    public float getMaxFitnessCondition() {
        return maxFitnessStopCondition.getRequiredFitness();
    }

    public void setMaxFitnessCondition(float stopConditionMaxFitness) {
        maxFitnessStopCondition.setRequiredFitness(stopConditionMaxFitness);
    }

    public long getTimeToStopCondition() {
        return timeStopCondition.getPeriodOfTime();
    }

    public void setTimeStopCondition(long periodInSeconds) {
        timeStopCondition.setPeriodOfTime(periodInSeconds);
    }

    // === Informative ===
    public  int getCurrentGeneration() {
        return evoEngine.getCurrentGeneration();
    }

    public void setUpdateGenerationInterval(int everyGens) {
        evoEngine.setUpdateGenerationInterval(everyGens);
    }

    public TimeTable getBestResult() {
        return (TimeTable) evoEngine.getBestSolution();
    }

    public Map<Integer, TimeTable> getHistoryGeneration2BestSolution() {
        return (Map<Integer, TimeTable>) ((TreeMap)evoEngine.getHistoryGeneration2BestSolution());
    }

    public void addStopCondition(StopCondition stopCondition) {
        if (this.state != State.RUNNING) {
            switch (stopCondition) {
                case MAX_GENERATIONS:
                    evoEngine.addStopCondition(StopCondition.MAX_GENERATIONS.name(), maxGenerationsStopCondition);
                    break;
                case REQUESTED_FITNESS:
                    evoEngine.addStopCondition(StopCondition.REQUESTED_FITNESS.name(), maxFitnessStopCondition);
                    break;
                case BY_TIME:
                    evoEngine.addStopCondition(StopCondition.BY_TIME.name(), timeStopCondition);
                    break;
            }
        }
    }

    public boolean isActiveStopCondition(StopCondition stopCondition) {
        return evoEngine.getStopConditions().containsKey(stopCondition.name());
    }

    public engine.base.stopConditions.StopCondition getStopCondition(StopCondition stopCondition) {
        switch (stopCondition) {
            case MAX_GENERATIONS:
                return maxGenerationsStopCondition;
            case REQUESTED_FITNESS:
                return maxFitnessStopCondition;
            case BY_TIME:
                return timeStopCondition;
        }

        return null;
    }

    public void removeStopCondition(StopCondition stopCondition) {
        if (this.state != State.RUNNING) {
            evoEngine.removeStopCondition(stopCondition.name());
        }
    }

    public void setElitism(int elitism) {
        evoEngine.setElitism(elitism);
    }

    public Engine() {
        this.multiThreaded = false;
        this.state = State.IDLE;
        this.evoEngineSettings = new evoEngineSettingsWrapper((TimeTableEvolutionEngine) this.evoEngine);
        this.factories = new Factories();

        maxGenerationsStopCondition = new MaxGenerationsStopCondition<>(evoEngine);
        maxFitnessStopCondition = new MaxFitnessStopCondition<>(evoEngine);
        timeStopCondition = new TimeStopCondition();
    }

    public void loadTTEEngineFromString(String xmlFileAsString) throws JAXBException, XMLExtractException {
        this.evoEngine = TTEvoEngineCreator.createEngineFromXMLString(xmlFileAsString);
        this.evoEngine.addFinishRunListener(this::algorithmFinished);

        this.isFileLoaded = true;
        this.state = State.IDLE;
    }

    public void loadTTEEngineWithProblem(Problem<TimeTable> problem) {
        this.evoEngine = TTEvoEngineCreator.createEngineFromProblem(problem);
        this.evoEngine.addFinishRunListener(this::algorithmFinished);

        this.isFileLoaded = true;
        this.state = State.IDLE;
    }

    public void changeCrossover(String crossoverName){
        this.evoEngine.setCrossover(factories.getCrossoverFactory().create(crossoverName));
    }

    public void changeSelection(String selectionName) {
        this.evoEngine.setSelection(factories.getSelectionFactory().create(selectionName));
    }

    public Set<Mutation<TimeTable>> getMutations() {
        return evoEngine.getMutations();
    }

    public void setPopulationSize(int popSize) {
        evoEngine.setPopulationSize(popSize);
    }

    public void startAlgorithm() {
        this.state = State.RUNNING;
        timeStopCondition.reset();
        this.evoEngine.runAlgorithm();
    }

    public void stopAlgorithm() {
        this.evoEngine.stopAlgorithm();
    }

    public void pauseAlgorithm() {
        this.evoEngine.pauseAlgorithm();
    }

    public void resumeAlgorithm() {
        this.state = State.RUNNING;
        timeStopCondition.pause();
        this.evoEngine.runAlgorithm();
    }

    private void algorithmFinished() {
        this.state = State.COMPLETED;
    }

    public void afterDeserialized() {
        this.evoEngine.addFinishRunListener(this::algorithmFinished);
    }
}
