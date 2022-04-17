package iso;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class GODeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String n = object.get("ObjectTag").getAsString();
        JsonArray com = object.getAsJsonArray("components");
        GameObject gameObject = new GameObject(n);
        for (JsonElement e:com) {
            Component c = context.deserialize(e,Component.class);
            gameObject.addComponent(c);
        }
        gameObject.transform = gameObject.getComponent(Transform.class);

        return gameObject;
    }
}
