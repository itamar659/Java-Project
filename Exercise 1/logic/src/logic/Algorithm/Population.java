package logic.Algorithm;

import java.util.Arrays;
import java.util.Comparator;

public class Population {

    private final Solution[] solutions;

    public Population(int size, boolean createNewPop) {
        this.solutions = new Solution[size];

        if (createNewPop) {
            for (int i = 0; i < size; i++) {
                this.solutions[i] = new Solution();
            }
        }
    }

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
        Arrays.sort(solutions, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                } else if (o1.getFitness() == o2.getFitness()) {
                    return  0;
                }
                return -1;
            }
        });
    }

    public int getSize() {
        return solutions.length;
    }
}