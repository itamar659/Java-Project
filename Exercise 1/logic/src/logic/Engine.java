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

    public enum State {
        IDLE, RUNNING, COMPLETED;
    }

    private boolean isFileLoaded;
    private State state;

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

    public  int getMaxGenerations() {
        return evoEngine.getMaxGenerations();
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

    public void startAlgorithm(int generations) {
        this.state = State.RUNNING;
        this.evoEngine.runAlgorithm(generations);
        this.state = State.COMPLETED;
    }
}
