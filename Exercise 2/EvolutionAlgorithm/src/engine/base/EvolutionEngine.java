package engine.base;

import engine.Listeners;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;


public abstract class EvolutionEngine<T> implements Serializable {

    protected int populationSize;
    protected Population<T> population;
    protected Selection<T> selection;
    protected Crossover<T> crossover;
    protected Set<Mutation<T>> mutations;
    protected Problem<T> problem;
    protected int elitism;

    private int currentGeneration;
    private Map<String, Supplier<Boolean>> stopConditions;
    private Solution<T> bestSolution;
    private Map<Integer, Float> historyGeneration2Fitness;

    protected boolean isRunning;
    protected boolean isPaused;
    private int updateGenerationInterval;
    private final Listeners generationEndListeners;
    private final Listeners finishRunListeners;

    public Population<T> getPopulation() {
        return population;
    }

    public Problem<T> getProblem() {
        return problem;
    }

    public void setProblem(Problem<T> problem) {
        this.problem = problem;
    }

    public Selection<T> getSelection() {
        return selection;
    }

    public void setSelection(Selection<T> selection) {
        this.selection = selection;
    }

    public Crossover<T> getCrossover() {
        return crossover;
    }

    public void setCrossover(Crossover<T> crossover) {
        this.crossover = crossover;
    }

    public Set<Mutation<T>> getMutations() {
        return mutations;
    }

    public void setMutations(Set<Mutation<T>> mutations) {
        this.mutations = mutations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getElitism() {
        return elitism;
    }

    public void setElitism(int elitism) {
        this.elitism = elitism;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public synchronized Map<Integer, Float> getHistoryGeneration2Fitness() {
        return (Map<Integer, Float>) ((TreeMap<Integer, Float>)historyGeneration2Fitness).clone();
    }

    public void setUpdateGenerationInterval(int updateGenerationInterval) {
        this.updateGenerationInterval = updateGenerationInterval;
    }

    public void addStopCondition(String id, Supplier<Boolean> stopCondition) {
        this.stopConditions.put(id, stopCondition);
    }

    public void removeStopCondition(String id) {
        if (!this.stopConditions.containsKey(id)) {
            throw new IllegalArgumentException("Cannot remove an id that not in the stop conditions map");
        }

        this.stopConditions.remove(id);
    }

    public synchronized Solution<T> getBestSolution() {
        return bestSolution;
    }

    private synchronized void setBestSolution(Solution<T> bestSolution) {
        this.bestSolution = bestSolution;
    }

    public void addGenerationEndListener(Runnable action) {
        generationEndListeners.add(action);
    }

    public void removeGenerationEndListener(Runnable action) {
        generationEndListeners.remove(action);
    }

    public void addFinishRunListener(Runnable action) {
        finishRunListeners.add(action);
    }

    public boolean containsFinishRunListener(Runnable action) {
        return finishRunListeners.contains(action);
    }

    public void removeFinishRunListener(Runnable action) {
        finishRunListeners.remove(action);
    }

    public EvolutionEngine() {
        this.elitism = 0;
        this.populationSize = 0;
        this.mutations = new HashSet<>();
        this.historyGeneration2Fitness = new TreeMap<>();
        this.updateGenerationInterval = 100;
        this.generationEndListeners = new Listeners();
        this.finishRunListeners = new Listeners();
        this.stopConditions = new HashMap<>();
    }

    public void clearHistory() {
        this.historyGeneration2Fitness.clear();
        this.population = null;
    }

    private synchronized void updateHistoryGeneration2Fitness(Integer gen, Float fitness) {
        historyGeneration2Fitness.put(gen, fitness);
    }

    public void runAlgorithm() {
        this.isRunning = true;
        if (!isPaused) {
            this.currentGeneration = 0;
            this.historyGeneration2Fitness.clear();
            setBestSolution(population.getBestSolutionFitness());
        }

        isPaused = false;
        boolean[] shouldStop = {false};
        stopConditions.values().forEach(condition -> {
            shouldStop[0] = true;
        });
        for (; isRunning && !isPaused && !shouldStop[0]; currentGeneration++) {

            setBestSolution(population.getBestSolutionFitness());

            // Call listeners
            if (currentGeneration % updateGenerationInterval == 0) {
                updateHistoryGeneration2Fitness(currentGeneration, population.getBestSolutionFitness().getFitness());
                onEndOfGeneration();
            }

            // Step 1 - check if finished
            if (population.getBestSolutionFitness().getFitness() >= 100.0f) {
                break;
            }

            // Step 1.9 - elitism
            population.sort();
            Population<T> elitePop = population.copySmallerPopulation(elitism);

            // Step 2 - selection
            Population<T> selected = selection.select(population, elitism);

            // Step 3 - crossover
            population = crossover.crossover(elitePop.mergePopulations(selected), populationSize - elitism);

            // Step 4 - mutate
            for (Mutation<T> mutation : this.mutations) {
                mutation.mutatePopulation(population, problem);
            }

            // Step 4.1 - Add the elite population
            population = population.mergePopulations(elitePop);

            // Check conditions
            stopConditions.values().forEach(condition -> {
                shouldStop[0] = true;
            });
        }

        setBestSolution(population.getBestSolutionFitness());
        updateHistoryGeneration2Fitness(currentGeneration, population.getBestSolutionFitness().getFitness());
        isRunning = false;

        onFinish();
    }

    public void stopAlgorithm() {
        isPaused = false;
        isRunning = false;
    }

    public void pauseAlgorithm() {
        isPaused = true;
        isRunning = false;
    }

    protected void onEndOfGeneration() {
        generationEndListeners.raiseEvent();
    }

    protected void onFinish() {
        finishRunListeners.raiseEvent();
    }
}
