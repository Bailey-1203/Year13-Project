package PhysicsEngine.Components;

import components.Component;
import org.joml.Vector2f;

public abstract class collider extends Component {
    private Vector2f off = new Vector2f();

    public Vector2f getOff() {
        return off;
    }

    public void setOff(Vector2f off) {
        this.off = off;
    }
}
