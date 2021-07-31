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
    private int currentGeneration;
    private Supplier<Boolean> stopCondition;
    private Solution<T> bestSolution;
    private Map<Integer, Float> historyGeneration2Fitness;

    private boolean isRunning;
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

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public synchronized Map<Integer, Float> getHistoryGeneration2Fitness() {
        return (Map<Integer, Float>) ((TreeMap<Integer, Float>)historyGeneration2Fitness).clone();
    }

    public void setUpdateGenerationInterval(int updateGenerationInterval) {
        this.updateGenerationInterval = updateGenerationInterval;
    }

    public void setStopCondition(Supplier<Boolean> stopCondition) {
        this.stopCondition = stopCondition;
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
        this.mutations = new HashSet<>();
        this.historyGeneration2Fitness = new TreeMap<>();
        this.updateGenerationInterval = 100;
        this.generationEndListeners = new Listeners();
        this.finishRunListeners = new Listeners();
    }

    public void clearHistory() {
        this.historyGeneration2Fitness = new TreeMap<>();
        this.population = null;
    }

    private synchronized void updateHistoryGeneration2Fitness(Integer gen, Float fitness) {
        historyGeneration2Fitness.put(gen, fitness);
    }

    public void runAlgorithm() {
        this.isRunning = true;
        this.historyGeneration2Fitness.clear();
        bestSolution = population.getBestSolutionFitness();

        for (currentGeneration = 0; isRunning && !stopCondition.get(); currentGeneration++) {

            setBestSolution(population.getBestSolutionFitness());

            // Call listeners
            if (currentGeneration % updateGenerationInterval == 0) {
                updateHistoryGeneration2Fitness(currentGeneration, population.getBestSolutionFitness().getFitness());
                onEndOfGeneration();
            }

            // Step 1 - check if finished
            if (population.getBestSolutionFitness().getFitness() >= 1.0f) {
                break;
            }

            // Step 2 - selection
            Population<T> selected = selection.select(population);

            // Step 3 - crossover
            population = crossover.crossover(selected, populationSize);

            // Step 4 - mutate
            for (Mutation<T> mutation : this.mutations) {
                mutation.mutatePopulation(population, problem);
            }
        }

        bestSolution = population.getBestSolutionFitness();
        updateHistoryGeneration2Fitness(currentGeneration, population.getBestSolutionFitness().getFitness());
        onFinish();
    }

    public void stopAlgorithm() {
        isRunning = false;
    }

    protected void onEndOfGeneration() {
        generationEndListeners.raiseEvent();
    }

    protected void onFinish() {
        finishRunListeners.raiseEvent();
    }
}
