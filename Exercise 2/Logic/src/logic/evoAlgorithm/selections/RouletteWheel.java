package logic.evoAlgorithm.selections;

import engine.base.Population;
import engine.base.Selection;
import engine.base.Solution;
import engine.configurable.Configuration;
import engine.configurable.ReadOnlyConfiguration;
import logic.timeTable.TimeTable;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Random;

public class RouletteWheel implements Selection<TimeTable> {

    private static final Random rand = new Random();

    private final Configuration configuration;

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

        System.out.printf("Roulette Wheel error. Couldn't find the solution at range %f of %f", selectFitness, rouletteSize);
        return population.getSolutionByIndex(population.getSize());
    }

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        //TODO : talk with itamar on this
    }

    @Override
    public String getConfigurableName() {
        return "RouletteWheel";
    }

    public RouletteWheel() {
        configuration = new Configuration(
                new AbstractMap.SimpleEntry<>("Configuration", "none")
        );
    }

}
