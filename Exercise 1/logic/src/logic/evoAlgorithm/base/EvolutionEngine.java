package logic.evoAlgorithm.base;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

// TODO: All the fields can move to data object.

// TODO: Can have a best solution field, and save a single solution after the algorithm finish. (and del population that takes 700kb)
public abstract class EvolutionEngine<T> implements Serializable {

    protected int populationSize;
    protected Population<T> population;
    protected Selection<T> selection;
    protected Crossover<T> crossover;
    protected Set<Mutation<T>> mutations;
    protected Problem<T> problem;
    private int currentGeneration;

    private Supplier<Boolean> stopCondition;

    private Map<Integer, Float> historyGeneration2Fitness;

    private int updateGenerationInterval;
    private transient Set<Runnable> generationEndListeners;
    private transient Set<Runnable> finishRunListeners;

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

    public Map<Integer, Float> getHistoryGeneration2Fitness() {
        return historyGeneration2Fitness;
    }

    public void setUpdateGenerationInterval(int updateGenerationInterval) {
        this.updateGenerationInterval = updateGenerationInterval;
    }

    public void setStopCondition(Supplier<Boolean> stopCondition) {
        this.stopCondition = stopCondition;
    }

    public void generationEndListener(Runnable action) {
        if (generationEndListeners == null) {
            this.generationEndListeners = new HashSet<>();
        }

        generationEndListeners.add(action);
    }

    public void finishRunListener(Runnable action) {
        if (finishRunListeners == null) {
            this.finishRunListeners = new HashSet<>();
        }

        finishRunListeners.add(action);
    }

    public EvolutionEngine() {
        this.mutations = new HashSet<>();
        this.historyGeneration2Fitness = new TreeMap<>();
        this.updateGenerationInterval = 100;
    }

    public void clearHistory() {
        this.historyGeneration2Fitness = new TreeMap<>();
        this.population = null;
    }

    public void runAlgorithm() {
        historyGeneration2Fitness.clear();

        for (currentGeneration = 0; !stopCondition.get(); currentGeneration++) {

            // Call listeners
            if (currentGeneration % updateGenerationInterval == 0) {
                // TODO Ex 2: Can cause problems if transfer the parents to the next generation. we change the reference
                historyGeneration2Fitness.put(currentGeneration, population.getBestSolutionFitness().getFitness());
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

        historyGeneration2Fitness.put(currentGeneration, population.getBestSolutionFitness().getFitness());
        onFinish();
    }

    protected void onEndOfGeneration() {
        for (Runnable action : generationEndListeners) {
            action.run();
        }
    }

    protected void onFinish() {
        for (Runnable action : finishRunListeners) {
            action.run();
        }
    }
}
