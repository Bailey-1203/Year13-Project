package iso;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utility.Constant;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseDetection {
    private static MouseDetection instance;
    private final boolean[] buttonpressed = new boolean[9];
    private final Vector2f ViewPos = new Vector2f();
    private final Vector2f ViewSize = new Vector2f();
    private double scr_x, scr_y;
    private double x_pos, y_pos;
    private boolean drag;
    private int mbdown = 0;

    private MouseDetection() {
        this.scr_x = 0.0;
        this.scr_y = 0.0;
        this.x_pos = 0.0;
        this.y_pos = 0.0;
    }

    public static MouseDetection get() {
        if (MouseDetection.instance == null) {
            MouseDetection.instance = new MouseDetection();
        }
        return MouseDetection.instance;
    }

    public static void mousePosCallback(long window, double x_pos, double y_pos) {
        if (get().mbdown > 0) {
            get().drag = true;
        }

        get().x_pos = x_pos;
        get().y_pos = y_pos;

    }

    public static void MouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().mbdown++;
            if (button < get().buttonpressed.length) {
                get().buttonpressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            get().mbdown--;
            if (button < get().buttonpressed.length) {
                get().buttonpressed[button] = false;
                get().drag = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double x_off, double y_off) {
        get().scr_x = x_off;
        get().scr_y = y_off;
    }

    public static void reset() {
        get().scr_x = 0;
        get().scr_y = 0;
    }

    public static float get_x() {
        return (float) get().x_pos;
    }

    public static float get_y() {
        return (float) get().y_pos;
    }

    public static float get_scrx() {
        return (float) get().scr_x;
    }

    public static float get_scry() {
        return (float) get().scr_y;
    }

    public static boolean dragging() {
        return get().drag;
    }

    public static Vector2f calWorldPos() {
        Camera cam = Window.getScene().camera();
        float Xcurr = get_x() - get().ViewPos.x;
        Xcurr = (Xcurr / get().ViewSize.x) * 2.0f - 1.0f;
        float Ycurr = get_y() - get().ViewPos.y;
        Ycurr = -((Ycurr / get().ViewSize.y) * 2.0f - 1.0f);
        Vector4f temp = new Vector4f(Xcurr, Ycurr, 0, 1);
        Matrix4f invV = new Matrix4f(cam.getView_inv_Mat());
        Matrix4f invP = new Matrix4f(cam.getProj_inv_Mat());
        temp.mul(invV.mul(invP));
        return new Vector2f(temp.x, temp.y);
    }

    public static float getWorldY() {
        return calWorldPos().y;
    }

    public static float getWorldX() {
        return calWorldPos().x;
    }

    public static boolean mousepressed(int button) {
        if (button < get().buttonpressed.length) {
            return get().buttonpressed[button];
        } else {
            return false;
        }
    }

    public static void setViewPos(Vector2f viewPos) {
        get().ViewPos.set(viewPos);
    }

    public static void setViewSize(Vector2f viewSize) {
        get().ViewSize.set(viewSize);
    }

    public static float getScrX() {
        return getScrPos().x;
    }

    public static float getScrY() {
        return getScrPos().y;
    }

    public static Vector2f getScrPos() {
        float Ycurr = get_y() - get().ViewPos.y;
        Ycurr = Constant.getScreenSize().y - ((Ycurr / get().ViewSize.y) * Constant.getScreenSize().y);
        float Xcurr = get_x() - get().ViewPos.x;
        Xcurr = (Xcurr / get().ViewSize.x) * Constant.getScreenSize().x;
        return new Vector2f(Xcurr, Ycurr);
    }

    public static void endFrame() {
        get().scr_x = 0.0f;
        get().scr_y = 0.0f;
    }
}
