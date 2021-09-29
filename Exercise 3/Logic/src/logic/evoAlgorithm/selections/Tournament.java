package logic.evoAlgorithm.selections;

import engine.base.Population;
import engine.base.Selection;
import engine.base.Solution;
import engine.base.configurable.Configurable;
import engine.base.configurable.Configuration;
import engine.base.configurable.ReadOnlyConfiguration;
import logic.timeTable.TimeTable;

import java.util.AbstractMap;
import java.util.Random;

public class Tournament implements Selection<TimeTable>, Configurable {

    private static final String PARAMETER_PTE = "pte";

    private static final Random rand = new Random();

    private final Configuration configuration;

    private float getPTE() {
        return Float.parseFloat(configuration.getParameter(PARAMETER_PTE));
    }

    @Override
    public String getName() {
        return "Tournament";
    }

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        if (parameterName.equals(PARAMETER_PTE)) {
            Float.parseFloat(value);
        } else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    public Tournament() {
        configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_PTE, "0.5")
        );
    }

    @Override
    public Population<TimeTable> select(Population<TimeTable> population, int reduceSize) {
        int newPopulationSize = population.getSize() - reduceSize;
        Population<TimeTable> newPopulation = population.initializeSubPopulation(newPopulationSize);

        for (int i = 0; i < newPopulationSize; i++) {
            Solution<TimeTable> partner1 = population.getSolutionByIndex(rand.nextInt(population.getSize()));
            Solution<TimeTable> partner2 = population.getSolutionByIndex(rand.nextInt(population.getSize()));

            Solution<TimeTable> bigger;
            Solution<TimeTable> smaller;
            if (partner1.getFitness() > partner2.getFitness()) {
                bigger = partner1;
                smaller = partner2;
            }
            else {
                bigger = partner2;
                smaller = partner1;
            }

            if (getPTE() < rand.nextFloat()) {
                    newPopulation.setSolutionByIndex(i, bigger);
            } else {
                    newPopulation.setSolutionByIndex(i, smaller);
            }
        }

        return newPopulation;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "pte=" + getPTE() +
                '}';
    }
}
