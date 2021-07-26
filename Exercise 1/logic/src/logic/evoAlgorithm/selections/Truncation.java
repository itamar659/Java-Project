package logic.evoAlgorithm.selections;

import logic.evoAlgorithm.base.Parameterizable;
import logic.evoAlgorithm.base.Population;
import logic.evoAlgorithm.base.Selection;

public class Truncation implements Selection, Parameterizable {

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
    public Population select(Population population) {
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
