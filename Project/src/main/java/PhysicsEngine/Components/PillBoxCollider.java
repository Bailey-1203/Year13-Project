package PhysicsEngine.Components;

import components.Component;
import iso.Window;
import org.joml.Vector2f;

public class PillBoxCollider extends Component {
    private final transient CircleCollider top = new CircleCollider();
    private final transient CircleCollider bot = new CircleCollider();
    private final transient BoxCollider boxCollider = new BoxCollider();
    private transient boolean resetFixture = false;

    public float Width = 0.1f;
    public float Height = 0.2f;
    public Vector2f off = new Vector2f();


    @Override
    public void start() {
        this.top.gameObject = this.gameObject;
        this.bot.gameObject = this.gameObject;
        this.boxCollider.gameObject = this.gameObject;
        RecalculateColliders();
    }

    public CircleCollider getTop() {
        return top;
    }

    public CircleCollider getBot() {
        return bot;
    }

    public BoxCollider getBoxCollider() {
        return boxCollider;
    }

    public void ResetFixture() {
        if (Window.getPhysics().isLocked()) {
            resetFixture = true;
            return;
        }
        resetFixture = false;

        if (gameObject != null) {
            RBody rBody = gameObject.getComponent(RBody.class);
            if (rBody != null) {
                Window.getPhysics().resetPillCollider(rBody, this);
            }
        }
    }

    @Override
    public void update(float dt) {
        if (resetFixture) {
            ResetFixture();
        }
    }

    @Override
    public void EditorUpdate(float dt) {
        top.EditorUpdate(dt);
        bot.EditorUpdate(dt);
        boxCollider.EditorUpdate(dt);

        if (resetFixture) {
            ResetFixture();
        }
    }


    public void setWidth(float width) {
        this.Width = width;
        RecalculateColliders();
        ResetFixture();
    }

    public void setHeight(float height) {
        this.Height = height;
        RecalculateColliders();
        ResetFixture();
    }

    public void RecalculateColliders() {
        float Radius = Width/4;
        float BHeight = Height-2*Radius;
        top.setRadius(Radius);
        bot.setRadius(Radius);
        top.setOff(new Vector2f(off).add(0,BHeight/4));
        bot.setOff(new Vector2f(off).sub(0,BHeight/4));
        boxCollider.setSize(new Vector2f(Width/2,BHeight/2));
    }
}
