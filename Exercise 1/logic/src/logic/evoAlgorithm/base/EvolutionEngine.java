package logic.evoAlgorithm.base;

import logic.actions.Action;

import java.util.HashSet;
import java.util.Set;

public abstract class EvolutionEngine {

    protected int populationSize;
    protected Population population;
    protected Selection selection;
    protected Crossover crossover;
    protected Set<Mutation> mutations;
    protected Problem problem;
    private int maxGenerations;
    private int currentGeneration;

    private int listenEveryGeneration;
    private final Set<Action> generationEndListeners;
    private final Set<Action> finishRunListeners; // TODO: Change names

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

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setListenEveryGeneration(int listenEveryGeneration) {
        this.listenEveryGeneration = listenEveryGeneration;
    }

    public void addMutation(Mutation mutation) {
        this.mutations.add(mutation);
    }

    public  void generationEndListener(Action action) {
        generationEndListeners.add(action);
    }

    public  void finishRunListener(Action action) {
        finishRunListeners.add(action);
    }

    public EvolutionEngine() {
        mutations = new HashSet<>();
        generationEndListeners = new HashSet<>();
        finishRunListeners = new HashSet<>();
        listenEveryGeneration = 100;
    }

    public void runAlgorithm(int generations) {
        maxGenerations = generations;

        for (currentGeneration = 1; currentGeneration <= generations; currentGeneration++) {

            // Call listeners
            if (currentGeneration % listenEveryGeneration == 0) {
                onEndOfGeneration();
            }

            // Step 1 - check if finished
            if (population.getBestSolutionFitness().getFitness() >= 1.0f) {
                break;
            }

            // Step 2 - selection
            population = selection.select(population);

            // Step 3 - crossover
            population = crossover.repopulateWithCrossover(population, populationSize);

            // Step 4 - mutate
            for (Mutation mutation : this.mutations) {
                mutation.mutatePopulation(population, problem);
            }
        }

        onFinish();
    }

    protected void onEndOfGeneration() {
        for (Action action : generationEndListeners) {
            action.execute();
        }
    }

    protected void onFinish() {
        for (Action action : finishRunListeners) {
            action.execute();
        }
    }
}
