package components;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Converter implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String t = object.get("type").getAsString();
        JsonElement element = object.get("properties");

        try {
            return context.deserialize(element, Class.forName(t));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("missing element :" + t, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        object.add("properties", context.serialize(src, src.getClass()));
        return object;
    }
}
