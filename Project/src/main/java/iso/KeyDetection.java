package iso;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyDetection {
    private static KeyDetection instance;
    private final boolean[] Key_Pressed = new boolean[350];
    private final boolean[] Key_InitPress = new boolean[350];

    private KeyDetection() {

    }

    public static void EndFrame() {
        Arrays.fill(get().Key_InitPress, false);
    }

    public static KeyDetection get() {
        if (KeyDetection.instance == null) {
            KeyDetection.instance = new KeyDetection();
        }

        return KeyDetection.instance;
    }

    public static void KeyCallBack(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().Key_Pressed[key] = true;
            get().Key_InitPress[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().Key_Pressed[key] = false;
            get().Key_InitPress[key] = false;
        }
    }

    public static boolean IsPressed(int code) {
        if (code < get().Key_Pressed.length) {
            return get().Key_Pressed[code];
        } else {
            return false;
        }
    }

    public static boolean IsInitPress(int code) {
        return get().Key_InitPress[code];
    }
}
