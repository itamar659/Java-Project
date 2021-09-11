package webEngine.gsonHelpers.gsonSerializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import engine.base.Selection;
import engine.base.configurable.Configurable;

import java.lang.reflect.Type;

public class SelectionSerializer<T> implements JsonSerializer<Selection<T>> {
    @Override
    public JsonElement serialize(Selection<T> tSelection, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", tSelection.getName());

        if (tSelection instanceof Configurable) {
            JsonObject configurations = new JsonObject();
            ((Configurable)tSelection).getConfiguration().getParameters().forEach(configurations::addProperty);
            jsonObject.add("configurations", configurations);
        }

        return jsonObject;
    }
}
