package logic.Algorithm.genericEvolutionAlgorithm.selections;

import logic.Algorithm.genericEvolutionAlgorithm.Population;
import logic.Algorithm.genericEvolutionAlgorithm.Selection;

public class Truncation implements Selection {

    private int topPercent;

    public int getTopPercent() {
        return topPercent;
    }

    public void setTopPercent(int topPercent) {
        if (1 <= topPercent && topPercent <= 100) {
            this.topPercent = topPercent;
        }
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
}
