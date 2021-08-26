package logic.evoAlgorithm.selections;

import engine.base.Population;
import engine.base.Selection;
import engine.base.Solution;
import logic.timeTable.TimeTable;

import java.util.Arrays;
import java.util.Random;

public class RouletteWheel implements Selection<TimeTable> {

    private static final Random rand = new Random();

    @Override
    public String getName() {
        return "Roulette Wheel";
    }

    @Override
    public Population<TimeTable> select(Population<TimeTable> population, int reduceSize) {
        double rouletteSize = Arrays.stream(population.getSolutions())
                .mapToDouble(Solution::getFitness)
                .sum();

        Population<TimeTable> newPopulation = population.initializeSubPopulation(population.getSize());
        for (int i = 0; i < population.getSize(); i++) {
            newPopulation.setSolutionByIndex(i, selectSolution(population, rouletteSize));
        }

        return newPopulation;
    }

    public Solution<TimeTable> selectSolution(Population<TimeTable> population, double rouletteSize) {
        double selectFitness = rand.nextDouble() * rouletteSize;
        double currentFitness = 0;

        for (int i = 0; i < population.getSize(); i++) {
            currentFitness += population.getSolutionByIndex(i).getFitness();
            if (currentFitness >= selectFitness) {
                return population.getSolutionByIndex(i);
            }
        }

        System.out.printf("Roulette Wheel error. Couldn't find the solution at range '%f' of '%f'.%s%n", selectFitness, rouletteSize);
        return population.getSolutionByIndex(population.getSize());
    }
}
