package logic.evoAlgorithm.base;

import java.util.Arrays;

public abstract class Population {

    protected Solution[] solutions;

    public Solution[] getSolutions() {
        return solutions;
    }

    public Solution getSolutionByIndex(int idx) {
        return solutions[idx];
    }

    public void setSolutionByIndex(int idx, Solution solution) {
        solutions[idx] = solution;
    }

    public Solution getBestSolutionFitness() {
        Solution bestFitness = solutions[0];

        for (int i = 1; i < solutions.length; i++) {
            if (bestFitness.getFitness() < solutions[i].getFitness()) {
                bestFitness = solutions[i];
            }
        }

        return bestFitness;
    }

    public void sort() {
        Arrays.sort(solutions, (o1, o2) -> {
            if (o1.getFitness() < o2.getFitness()) {
                return 1;
            } else if (o1.getFitness() == o2.getFitness()) {
                return  0;
            }
            return -1;
        });
    }

    public abstract Population copySmallerPopulation(int size);

    public abstract Population initializeSubPopulation(int size);

    public int getSize() {
        return solutions.length;
    }
}
