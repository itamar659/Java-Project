package webEngine.gsonHelpers.gsonStrategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.TimeTable;

public class SolutionExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getDeclaringClass() == TimeTable.class &&
               fieldAttributes.getDeclaredClass() == TimeTableProblem.class;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
