package components;

import GameEditor.PropertiesWindow;
import iso.MouseDetection;
import iso.Window;
import org.joml.Vector2f;
import utility.Constant;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MoveController extends ObjectController {


    public MoveController(Sprite spr, PropertiesWindow window) {
        super(spr, window);
    }

    @Override
    public void EditorUpdate(float dt) {
        if (Active != null) {
            if (xActive && !yActive) {
                Active.transform.Pos.x = MouseDetection.getWorldX() + XGap;
                needSnap = true;
            } else if (yActive) {
                Active.transform.Pos.y = MouseDetection.getWorldY() - YGap;
                needSnap = true;
            }
            if (Window.getImGuilayer().getEditorControl().getSnapToGrid() && !MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT) && needSnap) {
                Active.transform.Pos = new Vector2f(
                        Math.round((Active.transform.Pos.x) / Constant.getGridCount().x) * Constant.getGridCount().x,
                        Math.round((Active.transform.Pos.y) / Constant.getGridCount().y) * Constant.getGridCount().y);
                needSnap = false;
            }
        }

        super.EditorUpdate(dt);
    }
}
