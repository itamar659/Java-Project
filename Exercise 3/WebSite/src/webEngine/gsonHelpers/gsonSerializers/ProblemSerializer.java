package webEngine.gsonHelpers.gsonSerializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import engine.base.Problem;

import java.lang.reflect.Type;

public class ProblemSerializer<T> implements JsonSerializer<Problem<T>> {
    @Override
    public JsonElement serialize(Problem<T> tProblem, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(tProblem, tProblem.getClass());
    }
}
