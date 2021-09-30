package webEngine.gsonHelpers.gsonStrategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import engine.Listeners;
import engine.base.EvolutionEngine;
import engine.base.Population;
import logic.Engine;

public class EvolutionEngineExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        // Dont include listeners.
        if (fieldAttributes.getDeclaringClass() == EvolutionEngine.class &&
                fieldAttributes.getDeclaredClass() == Listeners.class) {
            return true;
        }

        // Dont include population (no need and could be huge)
        return fieldAttributes.getDeclaringClass() == EvolutionEngine.class &&
                fieldAttributes.getDeclaredClass() == Population.class;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
