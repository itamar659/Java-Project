package logic.Algorithm.genericEvolutionAlgorithm;

import java.util.HashSet;
import java.util.Set;

public abstract class EvolutionAlgorithm {

    protected int populationSize;
    protected Population population;
    protected Selection selection;
    protected Crossover crossover;
    protected Set<Mutation> mutations;
    protected Problem problem;

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

    public void addMutation(Mutation mutation) {
        this.mutations.add(mutation);
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public EvolutionAlgorithm() {
        mutations = new HashSet<>();
    }

    public void runAlgorithm(int generations) {

        // TODO: Remove the prints from logic (here)

        for (int i = 0; i < generations; i++) {
            if (population.getBestSolutionFitness().getFitness() >= 1.0f) break;

            System.out.println("Iter: " + i + "\nBest fitness: " + population.getBestSolutionFitness().getFitness());
            System.out.println(population.getBestSolutionFitness());

            // Step 2 - selection
            population = selection.select(population);

            // Step 3 - crossover
            population = crossover.repopulateWithCrossover(population, populationSize);

            // Step 4 - mutate
            for (Mutation mutation : this.mutations) {
                mutation.mutatePopulation(population, 0.1f, problem);
            }
        }

        System.out.println("Done!\nBest fitness: " + population.getBestSolutionFitness().getFitness());
        System.out.println(population.getBestSolutionFitness());
    }
}
