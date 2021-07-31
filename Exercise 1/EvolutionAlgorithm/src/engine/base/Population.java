package engine.base;

import java.io.Serializable;
import java.util.Arrays;

public abstract class Population<T> implements Serializable {

    protected Solution<T>[] solutions;

    public Solution<T>[] getSolutions() {
        return solutions;
    }

    public Solution<T> getSolutionByIndex(int idx) {
        return solutions[idx];
    }

    public void setSolutionByIndex(int idx, Solution<T> solution) {
        solutions[idx] = solution;
    }

    public Solution<T> getBestSolutionFitness() {
        Solution<T> bestFitness = solutions[0];

        for (int i = 1; i < solutions.length; i++) {
            if (bestFitness.getFitness() < solutions[i].getFitness()) {
                bestFitness = solutions[i];
            }
        }

        return bestFitness;
    }

    public void sort() {
        Arrays.sort(solutions, (o1, o2) -> Float.compare(o2.getFitness(), o1.getFitness()));
    }

    public abstract Population<T> copySmallerPopulation(int size);

    public abstract Population<T> initializeSubPopulation(int size);

    public int getSize() {
        return solutions.length;
    }
}
