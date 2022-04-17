package PhysicsEngine.Components;

import late.Drawer;
import org.joml.Vector2f;

public class CircleCollider extends collider {
    private float radius = 1f;

    public CircleCollider () {

    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void EditorUpdate(float dt) {
        Vector2f centre = new Vector2f(this.gameObject.transform.Pos).add(getOff());
        Drawer.addCircle(centre, this.radius);
    }
}
