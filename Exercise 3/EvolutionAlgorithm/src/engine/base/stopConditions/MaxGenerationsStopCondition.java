package engine.base.stopConditions;

import engine.base.EvolutionEngine;

public class MaxGenerationsStopCondition<T> implements StopCondition {

    private final EvolutionEngine<T> evolutionEngine;
    private int maxGenerations;

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public MaxGenerationsStopCondition(EvolutionEngine<T> evolutionEngine) {
        this.evolutionEngine = evolutionEngine;
    }

    @Override
    public boolean shouldStop() {
        //System.out.printf("Generation: %s / %s%n", evolutionEngine.getCurrentGeneration(), maxGenerations);
        return evolutionEngine.getCurrentGeneration() >= maxGenerations;
    }

    @Override
    public float getProgress() {
        if (maxGenerations > 0) {
            return evolutionEngine.getCurrentGeneration() / (float) maxGenerations;
        }
        return 0;
    }
}
