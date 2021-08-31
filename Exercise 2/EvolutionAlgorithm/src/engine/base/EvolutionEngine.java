package engine.base;

import engine.Listeners;
import engine.base.stopConditions.StopCondition;

import java.io.Serializable;
import java.util.*;


public abstract class EvolutionEngine<T> implements Serializable {

    protected int populationSize;
    protected Population<T> population;
    protected Selection<T> selection;
    protected Crossover<T> crossover;
    protected Set<Mutation<T>> mutations;
    protected Problem<T> problem;
    protected int elitism;

    private int currentGeneration;
    private Map<String, StopCondition> stopConditions;
    private Solution<T> bestSolution;
    private Map<Integer, Solution<T>> historyGeneration2Fitness;

    protected boolean isRunning;
    protected boolean isPaused;
    private int updateGenerationInterval;
    private final Listeners requiredIntervalListeners;
    private final Listeners everyGenerationListener;
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

    public synchronized Map<Integer, Solution<T>> getHistoryGeneration2BestSolution() {
        return (Map<Integer, Solution<T>>) ((TreeMap<Integer, Solution<T>>)historyGeneration2Fitness).clone();
    }

    public void setUpdateGenerationInterval(int updateGenerationInterval) {
        this.updateGenerationInterval = updateGenerationInterval;
    }

    public Map<String, StopCondition> getStopConditions() {
        return stopConditions;
    }

    public void addStopCondition(String id, StopCondition stopCondition) {
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

    public void addRequiredIntervalListener(Runnable action) {
        requiredIntervalListeners.add(action);
    }

    public void removeRequiredIntervalListener(Runnable action) {
        requiredIntervalListeners.remove(action);
    }

    public void addOnEveryGenerationEnd(Runnable action) {
        everyGenerationListener.add(action);
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
        this.requiredIntervalListeners = new Listeners();
        this.everyGenerationListener = new Listeners();
        this.finishRunListeners = new Listeners();
        this.stopConditions = new HashMap<>();
    }

    public void clearHistory() {
        this.historyGeneration2Fitness.clear();
        this.population = null;
    }

    private synchronized void updateHistoryGeneration2Fitness(Integer gen, Solution<T> solution) {
        historyGeneration2Fitness.put(gen, solution);
    }

    public void runAlgorithm() {
        this.isRunning = true;
        if (!isPaused) {
            this.currentGeneration = 0;
            this.historyGeneration2Fitness.clear();
        }

        setBestSolution(population.getBestSolutionFitness());

        isPaused = false;
        boolean[] shouldStop = {stopConditions.values().size() == 0};
        stopConditions.values().forEach(condition -> {
            if (condition.shouldStop()) shouldStop[0] = true;
        });
        for (; isRunning && !isPaused && !shouldStop[0]; currentGeneration++) {

            setBestSolution(population.getBestSolutionFitness());

            // Call listeners
            if (currentGeneration % updateGenerationInterval == 0) {
                updateHistoryGeneration2Fitness(currentGeneration, population.getBestSolutionFitness());
                onRequiredInterval();
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

            onEveryGenerationEnd();

            // Check conditions
            stopConditions.values().forEach(condition -> {
                if (condition.shouldStop()) shouldStop[0] = true;
            });
        }
        currentGeneration--; // Dont count the last generation that not started but stopped at.

        setBestSolution(population.getBestSolutionFitness());
        isRunning = false;

        if (!isPaused) {
            onFinish();
            updateHistoryGeneration2Fitness(currentGeneration, population.getBestSolutionFitness());
        }
    }

    public synchronized void stopAlgorithm() {
        isPaused = false;
        isRunning = false;
    }

    public synchronized void pauseAlgorithm() {
        isPaused = true;
        isRunning = false;
    }

    protected void onRequiredInterval() {
        requiredIntervalListeners.raiseEvent();
    }

    protected void onEveryGenerationEnd() {
        everyGenerationListener.raiseEvent();
    }

    protected void onFinish() {
        finishRunListeners.raiseEvent();
    }
}
