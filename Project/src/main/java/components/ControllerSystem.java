package components;

import iso.KeyDetection;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

public class ControllerSystem extends Component {
    private final SpriteSheet sprites;
    private int usingID = 0;

    public ControllerSystem(SpriteSheet sprites) {
        this.sprites = sprites;
    }

    @Override
    public void start() {

    }

    @Override
    public void EditorUpdate(float dt) {
        if (usingID == 0) {
            gameObject.getComponent(MoveController.class).setusable();
            gameObject.getComponent(ScaleController.class).setUnusable();
        } else if (usingID == 1) {
            gameObject.getComponent(MoveController.class).setUnusable();
            gameObject.getComponent(ScaleController.class).setusable();
        }

        if (KeyDetection.IsInitPress(GLFW_KEY_B) && usingID == 0) {
            usingID = 1;
        } else if (KeyDetection.IsInitPress(GLFW_KEY_B) && usingID == 1) {
            usingID = 0;
        }
    }
}
