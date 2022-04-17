package components;

import GameEditor.MImGui;
import iso.Camera;
import iso.KeyDetection;
import iso.MouseDetection;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCam extends Component {

    private final Camera EditorCamera;
    private final transient float ScrolSens = 0.1f;
    private final Vector2f campos;
    private final float reset;
    private transient Vector2f MouseOrigin;
    private transient float detectHeld = 0.032f;
    private float MouseSens = 10.0f;
    private transient float time = 0.0f;
    private transient boolean recentre = false;
    private transient boolean movetoobj = false;
    private float Zoom;


    public EditorCam(Camera camera) {
        this.EditorCamera = camera;
        this.MouseOrigin = new Vector2f();
        this.campos = camera.pos_vec;
        this.Zoom = camera.getZoom();
        this.reset = this.Zoom;
    }

    @Override
    public void EditorUpdate(float dt) {
        if (MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_MIDDLE) && detectHeld > 0) {
            this.MouseOrigin = new Vector2f(MouseDetection.calWorldPos());
            detectHeld -= dt;
        } else if (MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f MouseLoc = new Vector2f(MouseDetection.calWorldPos());
            Vector2f Change = new Vector2f(MouseLoc.sub(this.MouseOrigin));
            EditorCamera.pos_vec.sub(Change.mul(dt).mul(MouseSens));
            this.MouseOrigin.lerp(MouseLoc, dt);
        }

        if (detectHeld <= 0.0f && !MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
            detectHeld = 0.1f;
        }

        EditorCamera.setZoom(Zoom);

        if (KeyDetection.IsPressed(GLFW_KEY_C)) {
            recentre = true;
        } else if (KeyDetection.IsPressed(GLFW_KEY_M)) {
            movetoobj = true;
        }

        if (recentre) {
            EditorCamera.pos_vec.lerp(new Vector2f(), time);
            this.time += 0.1f * dt;

            if (Math.abs(EditorCamera.pos_vec.x) <= 5.0f && Math.abs(EditorCamera.pos_vec.y) <= 5.0f) {
                EditorCamera.pos_vec.set(0f, 0f);
                recentre = false;
                this.time = 0.0f;
            }
        } else if (movetoobj) {
            EditorCamera.pos_vec.lerp(gameObject.transform.Pos, time);
            this.time += 0.1f * dt;

            if (Math.abs(EditorCamera.pos_vec.x) <= gameObject.transform.Pos.x + 5.0f && Math.abs(EditorCamera.pos_vec.y) <= gameObject.transform.Pos.y + 5.0f) {
                EditorCamera.pos_vec.set(gameObject.transform.Pos.x, gameObject.transform.Pos.y);
                recentre = false;
                this.time = 0.0f;
            }
        }
    }

    @Override
    public void imgui() {
        MImGui.drawVec2Control("Camera Position:", campos);
        Zoom = MImGui.drawFLoatControl("Zoom :", Zoom, 0, 100, 0.01f, this.reset);
        MouseSens = MImGui.drawFLoatControl("Mouse sens", MouseSens, 0, 5, 0.1f, 1);
    }
}

