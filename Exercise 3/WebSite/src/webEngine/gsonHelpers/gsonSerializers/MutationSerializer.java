package webEngine.gsonHelpers.gsonSerializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import engine.base.Mutation;

import java.lang.reflect.Type;

public class MutationSerializer<T> implements JsonSerializer<Mutation<T>> {
    @Override
    public JsonElement serialize(Mutation<T> tMutation, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", tMutation.getName());

        JsonObject configurations = new JsonObject();
        tMutation.getConfiguration().getParameters().forEach(configurations::addProperty);
        jsonObject.add("configurations", configurations);

        return jsonObject;
    }
}
