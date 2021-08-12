package logic.evoAlgorithm.selections;

import logic.schema.Parameterizable;
import engine.base.Population;
import engine.base.Selection;
import logic.timeTable.TimeTable;

public class Truncation implements Selection<TimeTable>, Parameterizable {

    private int topPercent;

    public int getTopPercent() {
        return topPercent;
    }

    public void setTopPercent(int topPercent) {
        this.topPercent = topPercent;
    }

    public Truncation() {
        this.topPercent = 10; // default value
    }

    @Override
    public Population<TimeTable> select(Population<TimeTable> population) {
        int newPopulationSize = topPercent * population.getSize() / 100;
        population.sort();
        return population.copySmallerPopulation(newPopulationSize);
    }

    @Override
    public String toString() {
        return "Truncation{" +
                "topPercent=" + topPercent +
                '}';
    }

    @Override
    public void setValue(String parameterName, Object value) {
        if (parameterName.equals("TopPercent")) {
            topPercent = Integer.parseInt(value.toString());
        } else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }
    }

    @Override
    public Object getValue(String parameterName) {
        if (parameterName.equals("TopPercent")) {
            return topPercent;
        } else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }
    }
}
