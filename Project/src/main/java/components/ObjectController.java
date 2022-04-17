package components;

import GameEditor.PropertiesWindow;
import iso.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class ObjectController extends Component {
    Vector4f xColour, yColour, ColourH, Clear;
    GameObject xObj, yObj, Active;
    SpriteRenderer xTex, yTex;
    PropertiesWindow propertiesWindow;
    Vector2f objdie;
    boolean xhover, yhover, xActive, yActive, usable, needSnap, held, initpressed;
    float XGap, YGap;

    public ObjectController(Sprite texture, PropertiesWindow propertiesWindow1) {
        this.xColour = new Vector4f(1, 0, 0, 1);
        this.yColour = new Vector4f(0, 0, 1, 1);
        this.Clear = new Vector4f(0, 0, 0, 0);
        this.ColourH = new Vector4f(1, 1, 0, 1);
        this.Active = null;
        this.objdie = new Vector2f(0.125f, 0.375f);
        this.XGap = 0;
        this.YGap = 0;
        this.xhover = false;
        this.yhover = false;
        this.xActive = false;
        this.yActive = false;
        this.usable = false;
        this.needSnap = false;
        this.held = false;
        this.initpressed = false;
        this.propertiesWindow = propertiesWindow1;

        this.xObj = Prefabs.genObjectClicked(texture, objdie.x, objdie.y, 21);
        this.yObj = Prefabs.genObjectClicked(texture, objdie.x, objdie.y, 21);
        this.xTex = this.xObj.getComponent(SpriteRenderer.class);
        this.yTex = this.yObj.getComponent(SpriteRenderer.class);

        this.xObj.setNotSerialise();
        this.yObj.setNotSerialise();

        Window.getScene().addGOTS(this.xObj);
        Window.getScene().addGOTS(this.yObj);
    }

    @Override
    public void start() {
        this.xObj.transform.Rotation = 90.0f;
        this.yObj.transform.Rotation = 180.0f;
    }

    @Override
    public void EditorUpdate(float dt) {
        if (!usable) {
            return;
        }

        if (this.Active != Window.getScene().getActive()) {
            if (Window.getScene().getActive() != null) {
                this.setActive(Window.getScene().getActive());
            } else {
                this.setInActive();
                return;
            }
        }

        KeyControls();

        if (this.xhover && MouseDetection.dragging() && MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT)) {
            if (initpressed) {
                this.XGap = gameObject.transform.Pos.x - MouseDetection.getWorldX();
                this.initpressed = false;
            }
            System.out.println(XGap);
            this.xActive = true;
            this.yActive = false;
        } else if (this.yhover && MouseDetection.dragging() && MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT)) {
            if (initpressed) {
                this.YGap = gameObject.transform.Pos.y - MouseDetection.getWorldY();
                this.initpressed = false;
            }
            this.yActive = true;
            this.xActive = false;
        } else {
            this.initpressed = true;
            this.xActive = false;
            this.yActive = false;
        }

        if (this.Active != null) {
            this.xObj.transform.Pos.set(this.Active.transform.Pos.x + this.Active.transform.Sca.x,
                    this.Active.transform.Pos.y);
            this.yObj.transform.Pos.set(this.Active.transform.Pos.x,
                    this.Active.transform.Pos.y + this.Active.transform.Sca.y);
            if (!xActive && !yActive) {
                this.xhover = getxhover();
                this.yhover = getyhover();
            }

            if (xhover && !yhover) {
                setcolour(xTex, ColourH);
            } else if (yhover) {
                setcolour(yTex, ColourH);
            } else {
                setcolour(xTex, xColour);
                setcolour(yTex, yColour);
            }
        }
    }

    public void update(float dt) {
        if (usable) {
            setInActive();
        }
    }

    private void setActive(GameObject obj) {
        this.Active = obj;
        this.xTex.setColour(xColour);
        this.yTex.setColour(yColour);
    }

    private void setInActive() {
        this.Active = null;
        this.xTex.setColour(Clear);
        this.yTex.setColour(Clear);
    }

    private boolean getxhover() {
        Vector2f mouseloc = new Vector2f(MouseDetection.calWorldPos());
        Transform xtrans = this.xObj.transform;
        /*System.out.println((xtrans.Pos.x-xtrans.Sca.y/2)+","+(xtrans.Pos.x+xtrans.Sca.y/2)+"x");
        System.out.println(mouseloc.x+","+mouseloc.y+"mouse");
        System.out.println((xtrans.Pos.y-xtrans.Sca.x/2)+","+(xtrans.Pos.y+xtrans.Sca.x/2)+"y");*/
        if (mouseloc.x >= xtrans.Pos.x - xtrans.Sca.y / 2 && mouseloc.x <= xtrans.Pos.x + xtrans.Sca.y / 2) {
            return mouseloc.y >= xtrans.Pos.y - xtrans.Sca.x / 2 && mouseloc.y <= xtrans.Pos.y + xtrans.Sca.x / 2;
        } else {
            return false;
        }
    }

    private boolean getyhover() {
        Vector2f mouseloc = new Vector2f(MouseDetection.calWorldPos());
        Transform ytrans = this.yObj.transform;
        if (mouseloc.x >= ytrans.Pos.x - ytrans.Sca.x / 2 && mouseloc.x <= ytrans.Pos.x + ytrans.Sca.x / 2) {
            return mouseloc.y >= ytrans.Pos.y - ytrans.Sca.y / 2 && mouseloc.y <= ytrans.Pos.y + ytrans.Sca.y / 2;
        } else {
            return false;
        }
    }

    private void setcolour(SpriteRenderer spr, Vector4f col) {
        spr.setColour(col);
    }

    public boolean gethover() {
        return xhover | yhover;
    }

    public boolean checkhover() {
        return getyhover() | getxhover();
    }

    public void setusable() {
        this.usable = true;
    }

    public void setUnusable() {
        this.usable = false;
        this.setInActive();
    }

    private void KeyControls() {
        if (KeyDetection.IsPressed(GLFW_KEY_LEFT_SHIFT) && KeyDetection.IsInitPress(GLFW_KEY_D)) {
            //System.out.println("run");
            GameObject gameObject = this.Active.Duplicate();
            Window.getScene().addGOTS(gameObject);
            gameObject.transform.Pos.x += 0.25f;
            this.propertiesWindow.setActive(gameObject);
            Window.getScene().setActive(gameObject);
        } else if (KeyDetection.IsInitPress(GLFW_KEY_DELETE)) {
            Active.destroy();
            this.setInActive();
            this.propertiesWindow.setActive(null);
            Window.getScene().setActive(null);
        } else if (KeyDetection.IsInitPress(GLFW_KEY_G)) {
            Window.getScene().getEditor().getComponent(controller.class).setObjHeld(this.Active);
        } else if (KeyDetection.IsInitPress(GLFW_KEY_R)) {
            Window.getScene().getEditor().getComponent(controller.class).setRotationActive(this.Active);
        } else if (KeyDetection.IsInitPress(GLFW_KEY_DOWN)) {
            Active.transform.Pos.y -= 0.25;
        } else if (KeyDetection.IsInitPress(GLFW_KEY_UP)) {
            Active.transform.Pos.y += 0.25;
        } else if (KeyDetection.IsInitPress(GLFW_KEY_LEFT)) {
            Active.transform.Pos.x -= 0.25;
        } else if (KeyDetection.IsInitPress(GLFW_KEY_RIGHT)) {
            Active.transform.Pos.x += 0.25;
        } else if (KeyDetection.IsInitPress(GLFW_KEY_PAGE_UP)) {
            Active.transform.Zindex += 1;
        } else if (KeyDetection.IsInitPress(GLFW_KEY_PAGE_DOWN)) {
            Active.transform.Zindex -= 1;
        }
    }
}
