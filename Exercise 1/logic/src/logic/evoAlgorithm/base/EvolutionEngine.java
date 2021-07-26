package logic.evoAlgorithm.base;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

// TODO: All the fields can move to data object.
public abstract class EvolutionEngine implements Serializable {

    protected int populationSize;
    protected Population population;
    protected Selection selection;
    protected Crossover crossover;
    protected Set<Mutation> mutations;
    protected Problem problem;
    private int currentGeneration;

    private Supplier<Boolean> stopCondition;

    private final Map<Integer, Float> historyGeneration2Fitness;

    private int updateGenerationInterval;
    private transient Set<Runnable> generationEndListeners;
    private transient Set<Runnable> finishRunListeners;

    public Population getPopulation() {
        return population;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    public Set<Mutation> getMutations() {
        return mutations;
    }

    public void setMutations(Set<Mutation> mutations) {
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

    public void runAlgorithm() {
        historyGeneration2Fitness.clear();

        for (currentGeneration = 0; !stopCondition.get(); currentGeneration++) {

            // Call listeners
            if (currentGeneration % updateGenerationInterval == 0) {
                historyGeneration2Fitness.put(currentGeneration, population.getBestSolutionFitness().getFitness());
                onEndOfGeneration();
            }

            // Step 1 - check if finished
            if (population.getBestSolutionFitness().getFitness() >= 1.0f) {
                break;
            }

            // Step 2 - selection
            Population selected = selection.select(population);

            // Step 3 - crossover
            population = crossover.crossover(selected, populationSize);

            // Step 4 - mutate
            for (Mutation mutation : this.mutations) {
                mutation.mutatePopulation(population, problem);
            }
        }

        historyGeneration2Fitness.put(currentGeneration - 1, population.getBestSolutionFitness().getFitness());
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
