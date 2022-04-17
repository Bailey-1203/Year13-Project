package components;

import GameEditor.PropertiesWindow;
import iso.MouseDetection;
import iso.Window;
import org.joml.Vector2f;
import utility.Constant;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class ScaleController extends ObjectController {

    public ScaleController(Sprite spr, PropertiesWindow window) {
        super(spr, window);
    }

    public void EditorUpdate(float dt) {
        if (Active != null) {
            if (xActive && !yActive) {
                Active.transform.Sca.x = MouseDetection.getWorldX();
                needSnap = true;
            } else if (yActive) {
                Active.transform.Sca.y = MouseDetection.getWorldY();
                needSnap = true;
            }
            if (Window.getImGuilayer().getEditorControl().getSnapToGrid() && !MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT) && needSnap)
                Active.transform.Sca = new Vector2f(
                        Math.round((Active.transform.Sca.x) / Constant.getGridCount().x) * Constant.getGridCount().x,
                        Math.round((Active.transform.Sca.y) / Constant.getGridCount().y) * Constant.getGridCount().y);
            needSnap = false;
        }
        super.EditorUpdate(dt);
    }
}
