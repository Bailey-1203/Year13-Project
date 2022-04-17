package utility;

import iso.Window;
import org.joml.Vector2f;

public class Constant {

    private static final Vector2f GridCount = new Vector2f(0.25f,0.25f);

    private static Vector2f ScreenSize = new Vector2f(Window.getWidth(),Window.getHeight());

    private static Vector2f AspectRatio = new Vector2f(16.0f,9.0f);

    public static Vector2f getScreenSize() {
        return ScreenSize;
    }

    public static void setScreenSize(Vector2f NewSize) {
        ScreenSize = NewSize;
    }

    public static Vector2f getGridCount(){
        return GridCount;
    }

    public static float getScreenAspectRatio(){
        return AspectRatio.x/AspectRatio.y;
    }

    public static Vector2f getScreenAspectRatioV2() {
        return AspectRatio;
    }

    public static void setAspectRatio(Vector2f aspectRatio) {
        AspectRatio = aspectRatio;
    }

}
