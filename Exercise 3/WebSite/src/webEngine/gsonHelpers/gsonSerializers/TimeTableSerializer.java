package webEngine.gsonHelpers.gsonSerializers;

import com.google.gson.*;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rules;

import java.lang.reflect.Type;

public class TimeTableSerializer implements JsonSerializer<TimeTable> {
    @Override
    public JsonElement serialize(TimeTable tSolution, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fitness", tSolution.getFitness());
        jsonObject.addProperty("avgHardRules", tSolution.getAvgFitness(Rules.RULE_TYPE.HARD));
        jsonObject.addProperty("avgSoftRules", tSolution.getAvgFitness(Rules.RULE_TYPE.SOFT));
        jsonObject.add("lessons", jsonSerializationContext.serialize(tSolution.getLessons(), tSolution.getLessons().getClass()));
        jsonObject.add("rules", jsonSerializationContext.serialize(tSolution.getRules(), tSolution.getRules().getClass()));

        return jsonObject;
    }
}
