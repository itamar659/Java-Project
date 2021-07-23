package logic.evoAlgorithm.base;

import logic.actions.Action;
import logic.evoAlgorithm.timeTableEvolution.TimeTablePopulation;

import java.util.*;

public abstract class EvolutionEngine {

    protected int populationSize;
    protected Population population;
    protected Selection selection;
    protected Crossover crossover;
    protected Set<Mutation> mutations;
    protected Problem problem;
    private int maxGenerations;
    private int currentGeneration;

    private final Map<Integer, Float> historyGeneration2Fitness;

    private int updateGenerationInterval;
    private final Set<Action> generationEndListeners;
    private final Set<Action> finishRunListeners;

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

    public Map<Integer, Float> getHistoryGeneration2Fitness() {
        return historyGeneration2Fitness;
    }

    public void setUpdateGenerationInterval(int updateGenerationInterval) {
        this.updateGenerationInterval = updateGenerationInterval;
    }

    public void addMutation(Mutation mutation) {
        this.mutations.add(mutation);
    }

    public void generationEndListener(Action action) {
        generationEndListeners.add(action);
    }

    public void finishRunListener(Action action) {
        finishRunListeners.add(action);
    }

    public EvolutionEngine() {
        mutations = new HashSet<>();
        historyGeneration2Fitness = new TreeMap<>();
        generationEndListeners = new HashSet<>();
        finishRunListeners = new HashSet<>();
        updateGenerationInterval = 100;
    }

    public void runAlgorithm(int generations) {
        maxGenerations = generations;
        historyGeneration2Fitness.clear();

        for (currentGeneration = 0; currentGeneration <= generations; currentGeneration++) {

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
            Population children = crossover.crossover(selected, populationSize - selected.getSize());

            // Step 4 - mutate
            for (Mutation mutation : this.mutations) {
                mutation.mutatePopulation(children, problem);
            }

            newGeneration(population, selected, children);
        }

        historyGeneration2Fitness.put(currentGeneration - 1, population.getBestSolutionFitness().getFitness());
        onFinish();
    }

    private void newGeneration(Population population, Population parents, Population children) {
        int solIdx = 0;
        for (solIdx = 0; solIdx < parents.getSize(); solIdx++) {
            population.setSolutionByIndex(solIdx, parents.getSolutionByIndex(solIdx));
        }

        for (int i = 0; solIdx < populationSize; i++, solIdx++) {
            population.setSolutionByIndex(solIdx, children.getSolutionByIndex(i));
        }
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
