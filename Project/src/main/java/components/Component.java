package components;

import GameEditor.MImGui;
import imgui.ImGui;
import imgui.type.ImInt;
import iso.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector4i;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    private static int counterID = 0;
    private final String name = "";
    public transient GameObject gameObject = null;
    private int univID = -1;
    private boolean DisplayImGui = false;

    public static void init(int MaxID) {
        counterID = MaxID;
    }

    public void start() {

    }

    public void update(float delta_time) {

    }

    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field f : fields) {
                boolean tran = Modifier.isTransient(f.getModifiers());
                boolean priv = Modifier.isPrivate(f.getModifiers());

                if (tran) {
                    continue;
                }

                if (priv) {
                    f.setAccessible(true);
                }

                Class t = f.getType();
                Object data = f.get(this);
                String n = f.getName();

                if (data != null) {

                    if (t == int.class) {
                        int value = (int) data;
                        f.set(this, MImGui.drawIntControl(n + ":", value));
                    } else if (t == float.class) {
                        float val = (float) data;
                        f.set(this, MImGui.drawFLoatControl(n + ":", val));
                    } else if (t == Vector2f.class) {
                        MImGui.drawVec2Control(n + ":", (Vector2f) data);
                    } else if (t == boolean.class) {
                        boolean value = (boolean) data;
                        f.set(this, MImGui.drawCheckBox(n + ":", value));
                    } else if (t.isEnum()) {
                        String[] Enums = getEnums(t);
                        String Type = ((Enum) data).name();
                        ImInt index = new ImInt(IndexOf(Type, Enums));
                        if (ImGui.combo(f.getName(), index, Enums, Enums.length)) {
                            f.set(this, t.getEnumConstants()[index.get()]);
                        }
                    }


                    if (priv) {
                        f.setAccessible(false);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void GenUnivID() {
        if (this.univID == -1) {
            this.univID = counterID++;
        }
    }

    public int getUnivID() {
        return this.univID;
    }

    private <T extends Enum<T>> String[] getEnums(Class<T> type) {
        String[] Enums = new String[type.getEnumConstants().length];
        int i = 0;
        for (T enumval : type.getEnumConstants()) {
            Enums[i] = enumval.name();
            i++;
        }
        return Enums;
    }

    private int IndexOf(String string, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (string.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public boolean getDisplayImGui() {
        return DisplayImGui;
    }

    public void setDisplayImGui(boolean boo) {
        this.DisplayImGui = boo;
    }

    public String getName() {
        return name;
    }

    public void EditorUpdate(float dt) {

    }

    public void destroy() {

    }

    public Vector4i getdata() {
        return new Vector4i();
    }

    public void BeginCollision(GameObject obj, Contact contact, Vector2f Normal) {

    }

    public void EndCollision(GameObject obj, Contact contact, Vector2f Normal) {

    }

    public void PreSolve(GameObject obj, Contact contact, Vector2f Normal) {

    }

    public void PostSolve(GameObject obj, Contact contact, Vector2f Normal) {

    }
}
