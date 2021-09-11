package webEngine.gsonHelpers.gsonSerializers;

import com.google.gson.*;
import engine.base.Crossover;

import java.lang.reflect.Type;

public class CrossoverSerializer<T> implements JsonSerializer<Crossover<T>> {
    @Override
    public JsonElement serialize(Crossover<T> tCrossover, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", tCrossover.getName());

        JsonObject configurations = new JsonObject();
        tCrossover.getConfiguration().getParameters().forEach(configurations::addProperty);
        jsonObject.add("configurations", configurations);

        return jsonObject;
    }
}
