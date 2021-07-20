package logic.Algorithm.selections;

import logic.Algorithm.genericEvolutionAlgorithm.Population;
import logic.Algorithm.genericEvolutionAlgorithm.Selection;
import logic.validation.ValidationResult;

public class Truncation implements Selection {

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

    @Override
    public ValidationResult checkValidation() {
        if (!(1 <= topPercent && topPercent <= 100)) {
            return new ValidationResult(false, "'TopPercent' should be a value between 1 to 100");
        }

        return new ValidationResult(true);
    }
}
