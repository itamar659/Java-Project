package webEngine.gsonHelpers.gsonSerializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import engine.base.configurable.Configurable;
import logic.timeTable.rules.base.Rule;

import java.lang.reflect.Type;

public class RuleSerializer<T> implements JsonSerializer<Rule<T>> {
    @Override
    public JsonElement serialize(Rule<T> tRule, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", tRule.getType().toString());
        jsonObject.addProperty("name", tRule.getId());

        if (tRule instanceof Configurable) {
            JsonObject configurations = new JsonObject();
            ((Configurable)tRule).getConfiguration().getParameters().forEach(configurations::addProperty);
            jsonObject.add("configurations", configurations);
        }

        return jsonObject;
    }
}
