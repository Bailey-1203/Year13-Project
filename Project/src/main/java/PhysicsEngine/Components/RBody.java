package PhysicsEngine.Components;

import PhysicsEngine.enums.ObjectBodyType;
import components.Component;
import components.SpriteRenderer;
import iso.Window;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class RBody extends Component {
    private final Vector2f vel = new Vector2f();
    private float angularDamp = 0.8f;
    private float linearDamp = 0.9f;
    private float mass = 0;
    private ObjectBodyType bodyType = ObjectBodyType.Dynamic;
    private float Friction = 0.1f;
    private float angularVelocity = 0.0f;
    private float gravityScale = 1.0f;
    private boolean isSensor = false;

    private boolean FixedRot = true;
    private boolean ContinousCol = true;

    private transient Body body = null;

    public RBody() {

    }

    @Override
    public void update(float dt) {
        if (body != null) {
            this.gameObject.transform.Pos.set(body.getPosition().x, body.getPosition().y);
            this.gameObject.transform.Rotation = (float) Math.toDegrees(body.getAngle());
            this.gameObject.getComponent(SpriteRenderer.class).needReload();
        }
    }

    public Vector2f getVel() {
        return vel;
    }

    public void setVel(Vector2f vel) {
        this.vel.set(vel);
        if (body != null) {
            this.body.setLinearVelocity(new Vec2(vel.x, vel.y));
        }
    }

    public float getAngularDamp() {
        return angularDamp;
    }

    public void setAngularDamp(float angularDamp) {
        this.angularDamp = angularDamp;
    }

    public float getLinearDamp() {
        return linearDamp;
    }

    public void setLinearDamp(float linearDamp) {
        this.linearDamp = linearDamp;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public ObjectBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(ObjectBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isFixedRot() {
        return FixedRot;
    }

    public void setFixedRot(boolean fixedRot) {
        FixedRot = fixedRot;
    }

    public boolean isContinousCol() {
        return ContinousCol;
    }

    public void setContinousCol(boolean continousCol) {
        ContinousCol = continousCol;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isSensor() {
        return isSensor;
    }

    public void setSensor() {
        isSensor = true;
        if (body != null) {
            Window.getPhysics().setIsSensor(this);
        }
    }

    public void setNotSensor() {
        isSensor = false;
        if (body != null) {
            Window.getPhysics().setNotSensor(this);
        }
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if (body != null) {
            this.body.setGravityScale(gravityScale);
        }
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if (body != null) {
            this.body.setAngularVelocity(angularVelocity);
        }
    }

    public float getFriction() {
        return Friction;
    }

    public void setFriction(float friction) {
        Friction = friction;
    }

    public void addVelocity(Vector2f add) {
        if (body != null) {
            body.applyForceToCenter(new Vec2(vel.x, vel.y));
        }
    }

    public void addImpulse(Vector2f add) {
        if (body != null) {
            body.applyLinearImpulse(new Vec2(vel.x, vel.y), body.getWorldCenter());
        }
    }
}
