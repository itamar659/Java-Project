package engine.base.stopConditions;

import engine.base.EvolutionEngine;

public class MaxFitnessStopCondition<T> implements StopCondition {

    private final EvolutionEngine<T> evolutionEngine;
    private float requiredFitness;

    public float getRequiredFitness() {
        return requiredFitness;
    }

    public void setRequiredFitness(float requiredFitness) {
        this.requiredFitness = requiredFitness;
    }

    public MaxFitnessStopCondition(EvolutionEngine<T> evolutionEngine) {
        this.evolutionEngine = evolutionEngine;
    }

    @Override
    public boolean shouldStop() {
        if (evolutionEngine.getBestSolution() != null) {
            return evolutionEngine.getBestSolution().getFitness() >= requiredFitness;
        }
        return false;
    }

    @Override
    public float getProgress() {
        if (requiredFitness > 0 && evolutionEngine.getBestSolution() != null) {
            return evolutionEngine.getBestSolution().getFitness() / requiredFitness;
        }
        return 0;
    }
}
