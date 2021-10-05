package webEngine.gsonHelpers.gsonStrategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import engine.base.stopConditions.MaxFitnessStopCondition;
import webEngine.users.User;

import java.util.Map;

public class UserStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getDeclaredClass() == User.class &&
                (fieldAttributes.getDeclaringClass() == Map.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
