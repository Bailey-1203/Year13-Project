package PhysicsEngine.Components;

import late.Drawer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BoxCollider extends collider {
    private Vector2f size = new Vector2f(0.24f);
    private final Vector2f origin = new Vector2f();

    public BoxCollider() {

    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    @Override
    public void EditorUpdate(float dt) {
        Vector2f mid = new Vector2f(this.gameObject.transform.Pos);
        Drawer.addBox(mid,this.size,this.gameObject.transform.Rotation,new Vector3f(0,1,0));
    }
}
