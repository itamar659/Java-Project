package logic.Algorithm;

import java.util.Arrays;
import java.util.Random;

public class EvolutionAlgorithm {

    // Properties:
    private static final Random rand = new Random();

    private int populationSize;
    private Population population;
    private Selection selectionOperator;

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public EvolutionAlgorithm() {
        this.population = null;
        this.selectionOperator = new Truncation();
    }

    public void runAlgorithm() {
        // Set parameters. Why????????
        populationSize = 1000;
        Solution.setMaxValues(new int[]{100, 200, 300, 400, 500, 600, 700, 800, 900, 1000});

        // Step 1 - Initialize the population
        population = new Population(populationSize, true);

        for (int i = 0; i < 5000; i++) {
            if (population.getBestSolutionFitness().getFitness() == 1) break;

            System.out.println("Iter: " + i + "\nBest fitness: " + population.getBestSolutionFitness().getFitness());

            // Step 2 - Selection
            population = selectionOperator.execute(population, 20);
            //population = truncation(population, 20);

            // Step 3 - Crossover
            population = repopulate(population, populationSize);

            // Step 4 - Mutation
            mutationPopulation(population);
        }

        System.out.println("Best fitness: " + population.getBestSolutionFitness().getFitness());
        System.out.println(population.getBestSolutionFitness());

    }

    private void mutationPopulation(Population population) {
        float chance = 0.6f;
        for (Solution solution : population.getSolutions()) {
            if (rand.nextFloat() <= chance) {
                mutate(solution);
            }
        }
    }

    // Very basic
    private void mutate(Solution solution) {
        float chance = 0.5f;
        for (int i = 0; i < Solution.getMaxValues().length; i++) {
            if (rand.nextFloat() < chance) {
                solution.setGen(i, rand.nextInt(Solution.getMaxValues()[i] + 1));
            }
        }
    }

//    public Population truncation(Population population, int topPercent) {
//        int picks = topPercent * population.getSize() / 100;
//        Population survivors = new Population(picks, false);
//
//        population.sort();
//        for (int i = 0; i < picks; i++) {
//            survivors.setSolutionByIndex(i, population.getSolutionByIndex(i));
//        }
//
//        return survivors;
//    }

    public Population repopulate(Population population, int reachSize) {
        Population newPopulation = new Population(reachSize, false);

        for (int i = 0; i < population.getSize(); i++) {
            newPopulation.setSolutionByIndex(i, population.getSolutionByIndex(i));
        }

        Solution father = null;
        Solution mother = null;

        for (int i = population.getSize(); i < reachSize; i++) {
            if (i % 2 == 0 || i == population.getSize()) {
                int fatherIdx = rand.nextInt(population.getSize());
                int motherIdx = rand.nextInt(population.getSize());
                father = population.getSolutionByIndex(fatherIdx);
                mother = population.getSolutionByIndex(motherIdx);
            }

            newPopulation.setSolutionByIndex(i, crossover(father, mother));
        }

        return newPopulation;
    }

    public Solution crossover(Solution sol1, Solution sol2) {
        Solution child = new Solution();

        for (int i = 0; i < Solution.getMaxValues().length; i++) {
            if (i % 2 == 0) {
                child.setGen(i, sol1.getGen(i));
            } else {
                child.setGen(i, sol2.getGen(i));
            }
        }

        return child;
    }
}
