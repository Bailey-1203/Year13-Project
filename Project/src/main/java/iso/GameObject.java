package iso;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.Converter;
import components.SpriteRenderer;
import imgui.ImGui;
import org.joml.Vector2f;
import utility.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private static int CounterID = 0;
    private final List<Component> components;
    public transient Transform transform;
    private int univID;
    private String ObjectTag;
    private boolean serialise;
    private boolean dead;

    public GameObject(String obj_class) {
        this.ObjectTag = obj_class;
        this.components = new ArrayList<>();
        this.univID = CounterID++;
        this.serialise = true;
        this.dead = false;
    }

    public static void init(int MaxID) {
        CounterID = MaxID;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void setNotSerialise() {
        this.serialise = false;
    }

    public void addComponent(Component c) {
        c.GenUnivID();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float delta_time) {
        for (Component component : components) {
            component.update(delta_time);
        }
    }

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public void imgui() {
        for (Component com : components) {
            if (com.getDisplayImGui()) {
                if (ImGui.collapsingHeader(com.getClass().getSimpleName())) {
                    com.imgui();
                }
            }
        }
    }

    public int getUnivID() {
        return this.univID;
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean getSerialise() {
        return this.serialise;
    }

    public void destroy() {
        this.dead = true;
        for (Component c : components) {
            c.destroy();
        }
    }

    public void EditorUpdate(float dt) {
        for (Component component : components) {
            component.EditorUpdate(dt);
        }
    }

    public GameObject Duplicate() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new Converter())
                .registerTypeAdapter(GameObject.class, new GODeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String obj = gson.toJson(this);
        GameObject gameObject = gson.fromJson(obj, GameObject.class);
        gameObject.genNewUID();
        for (Component component : gameObject.getComponents()) {
            component.GenUnivID();
        }
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if (spriteRenderer != null && spriteRenderer.getTexture() != null) {
            spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilePath()));
        }

        return gameObject;
    }

    public boolean isDead() {
        return dead;
    }

    public void genNewUID() {
        this.univID = CounterID++;
    }

    public String getObjectName() {
        return this.ObjectTag;
    }

    public void setObjectName(String tag) {
        this.ObjectTag = tag;
    }
}

