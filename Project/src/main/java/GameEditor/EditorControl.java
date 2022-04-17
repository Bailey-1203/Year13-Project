package GameEditor;

import imgui.ImGui;
import iso.Window;
import org.joml.Vector2f;
import utility.Constant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EditorControl {
    private static int Vindex = 2;
    private static final float opacity = 1.0f;
    private static final boolean viewgrid = true;
    private static final boolean snaptogrid = true;
    private static final Vector2f AspectRatio = Constant.getScreenAspectRatioV2();
    private transient int Vindexlast = 2;
    private transient float opacitylast = 1.0f;

    public EditorControl() {

    }

    public void imgui() {
        if (Window.getScene().getEditor() != null) {
            ImGui.begin("Level Editor");
            if (ImGui.collapsingHeader("Editor Control")) {
                runImGui();
            }
            Window.getScene().getEditor().imgui();
            ImGui.end();
        }

        if (Vindex != Vindexlast) {
            Window.getScene().getSceneConstructor().updateGameobjCol(Vindex, opacity);
            Window.getScene().getSceneConstructor().getClicked();
        } else if (opacity != opacitylast) {
            Window.getScene().getSceneConstructor().updateGameobjCol(Vindex, opacity);
        }

        Vindexlast = Vindex;
        opacitylast = opacity;
    }

    private void runImGui() {
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

                Class<?> t = f.getType();
                Object data = f.get(this);
                String n = f.getName();

                if (t == int.class) {
                    int value = (int) data;
                    f.set(this, MImGui.drawIntControl(n + ":", value, 0, 20));
                } else if (t == float.class) {
                    float val = (float) data;
                    f.set(this, MImGui.drawFLoatControl(n + ":", val, 0, 1, 0.01f));
                } else if (t == Vector2f.class) {
                    MImGui.drawVec2Control(n + ":", (Vector2f) data);
                } else if (t == boolean.class) {
                    f.set(this, MImGui.drawCheckBox(n + ":", (Boolean) data));
                }
                if (priv) {
                    f.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (AspectRatio.x != Constant.getScreenAspectRatioV2().x && AspectRatio.y != Constant.getScreenAspectRatioV2().y) {
            Constant.setAspectRatio(AspectRatio);
        }
    }

    public int getVindex() {
        return Vindex;
    }

    public boolean getviewgrid() {
        return viewgrid;
    }

    public boolean getSnapToGrid() {
        return snaptogrid;
    }
}
