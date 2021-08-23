package logic.evoAlgorithm.selections;

import engine.base.*;
import engine.configurable.Configurable;
import engine.configurable.Configuration;
import engine.configurable.ReadOnlyConfiguration;
import logic.timeTable.TimeTable;

import java.util.AbstractMap;

public class Truncation implements Selection<TimeTable>{

    private static final String PARAMETER_TOP_PERCENT = "TopPercent";

    private final Configuration configuration;

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        if (parameterName.equals(PARAMETER_TOP_PERCENT)) {
            Integer.parseInt(value);
        } else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    @Override
    public String getConfigurableName() {
        return "Truncation";
    }

    public Truncation() {
        configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_TOP_PERCENT, "10")
        );
    }


    public int getTopPercent() {
        return Integer.parseInt(configuration.getParameter(PARAMETER_TOP_PERCENT));
    }

    @Override
    public Population<TimeTable> select(Population<TimeTable> population, int reduceSize) {
        int newPopulationSize = getTopPercent() * population.getSize() / 100 - reduceSize;
        if (newPopulationSize > 0) {
            return population.copySmallerPopulation(newPopulationSize);
        }
        return population.copySmallerPopulation(0);
    }

    @Override
    public String toString() {
        return "Truncation{" +
                "topPercent=" + getTopPercent() +
                '}';
    }
}
