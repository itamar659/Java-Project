package webEngine.gsonHelpers.gsonStrategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import engine.Listeners;
import engine.base.EvolutionEngine;
import logic.Engine;

public class EvolutionEngineExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getDeclaringClass() == EvolutionEngine.class &&
                fieldAttributes.getDeclaredClass() == Listeners.class;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
