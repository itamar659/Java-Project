package webEngine.gsonHelpers.gsonStrategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import engine.base.EvolutionEngine;
import engine.base.stopConditions.MaxFitnessStopCondition;
import engine.base.stopConditions.MaxGenerationsStopCondition;

public class StopConditionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getDeclaredClass() == EvolutionEngine.class &&
                (fieldAttributes.getDeclaringClass() == MaxGenerationsStopCondition.class ||
                fieldAttributes.getDeclaringClass() == MaxFitnessStopCondition.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
