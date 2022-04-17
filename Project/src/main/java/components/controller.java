package components;

import iso.GameObject;
import iso.KeyDetection;
import iso.MouseDetection;
import iso.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utility.Cal;
import utility.Constant;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class controller extends Component {
    GameObject objHeld = null;
    Vector2f win_dimensions, pos;
    Vector2f Size, MousePos, MousePosLast;
    Boolean RotationActive, held;


    public controller() {
        this.win_dimensions = new Vector2f(Window.getWidth(), Window.getHeight());
        this.Size = Constant.getGridCount();
        this.RotationActive = false;
        this.held = false;
    }

    public void objPicked(GameObject objClicked) {
        this.objHeld = objClicked;
        this.objHeld.getComponent(SpriteRenderer.class).setColour(new Vector4f(0.8f, 0.8f, 0.8f, 0.8f));
        Window.getScene().addGOTS(objHeld);
        this.RotationActive = false;
    }

    public void objPlaced() {
        this.pos = objHeld.transform.Pos;
        if (Window.getImGuilayer().getEditorControl().getSnapToGrid()) {
            Vector2f newpos = new Vector2f(Math.round(this.pos.x / Size.x) * Size.x, Math.round(this.pos.y / Size.y) * Size.y);
            objHeld.transform.Pos.x = newpos.x;
            objHeld.transform.Pos.y = newpos.y;
        }
        if (this.objHeld.getComponent(SpriteState.class) != null) {
            this.objHeld.getComponent(SpriteState.class).refreshTextures();
        }
        this.objHeld.getComponent(SpriteRenderer.class).setColour(new Vector4f(1f, 1f, 1f, 1f));
        GameObject temp = objHeld.Duplicate();
        objHeld = null;
        this.RotationActive = false;
        objPicked(temp);

    }

    public void setObjHeld(GameObject gameObject) {
        if (this.objHeld == null) {
            this.objHeld = gameObject;
            this.Size = Constant.getGridCount();
            this.RotationActive = false;
        }
    }

    @Override
    public void EditorUpdate(float delta_time) {
        if (objHeld != null) {
            if (!RotationActive) {
                UpdateGameObjcetPos();
            } else {
                UpdateGameObjcetRotation();
            }
        }
    }

    public void setRotationActive(GameObject gameObject) {
        if (this.objHeld == null) {
            this.RotationActive = true;
            this.objHeld = gameObject;
            this.MousePos = new Vector2f(MouseDetection.calWorldPos());
            this.MousePosLast = this.MousePos;
        }
    }

    public void UpdateGameObjcetPos() {
        if (Window.getImGuilayer().getEditorControl().getSnapToGrid()) {
            objHeld.transform.Pos.x = Math.round(MouseDetection.getWorldX() / Size.x) * Size.x;
            objHeld.transform.Pos.y = Math.round(MouseDetection.getWorldY() / Size.y) * Size.y;
        } else {
            objHeld.transform.Pos.x = MouseDetection.getWorldX();
            objHeld.transform.Pos.y = MouseDetection.getWorldY();
        }

        if (MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT) && !held) {
            objPlaced();
            held = true;
        } else if (KeyDetection.IsPressed(GLFW_KEY_ESCAPE) && objHeld != null) {
            objHeld.destroy();
            objHeld = null;
        }
        if (!MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT)) {
            held = false;
        }
    }

    public void UpdateGameObjcetRotation() {
        this.MousePos = new Vector2f(MouseDetection.getWorldX() - objHeld.transform.Pos.x,
                MouseDetection.getWorldY() - objHeld.transform.Pos.y);

        Vector2f objcetPos = new Vector2f(0, 1);
        //System.out.println(objcetPos.x+","+objcetPos.y+","+MousePos.x+","+MousePos.y);
        float angle = Cal.AngleBetweenVectors(this.MousePos, objcetPos);

        if (MousePos.x < 0) {
            objHeld.transform.Rotation = (float) Math.toDegrees(angle);
        } else {
            objHeld.transform.Rotation = -(float) Math.toDegrees(angle);
        }

        if (MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT)) {
            objPlaced();
        }
    }

}
