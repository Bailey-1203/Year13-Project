package PhysicsEngine;

import PhysicsEngine.Components.BoxCollider;
import PhysicsEngine.Components.CircleCollider;
import PhysicsEngine.Components.PillBoxCollider;
import PhysicsEngine.Components.RBody;
import iso.GameObject;
import iso.Transform;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

public class Physics {

    private final Vec2 gravity = new Vec2(0, -15.0f);
    private final World world = new World(gravity);
    private float time = 0.0f;

    public Physics() {
        world.setContactListener(new ContactListerner());
    }

    public void update(float dt) {
        time += dt;
        if (time >= 0.0f) {
            float timechange = 1.0f / 60.0f;
            time -= timechange;
            int vel = 8;
            int pos = 3;
            world.step(timechange, vel, pos);
        }
    }

    public void removeObject(GameObject gameObject) {
        RBody rBody = gameObject.getComponent(RBody.class);
        if (rBody != null) {
            if (rBody.getBody() != null) {
                world.destroyBody(rBody.getBody());
                rBody.setBody(null);
            }
        }
    }

    public void add(GameObject gameObject) {
        RBody rBody = gameObject.getComponent(RBody.class);
        if (rBody != null && rBody.getBody() == null) {
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.Rotation);
            bodyDef.position.set(transform.Pos.x, transform.Pos.y);
            bodyDef.angularDamping = rBody.getAngularDamp();
            bodyDef.linearDamping = rBody.getLinearDamp();
            bodyDef.fixedRotation = rBody.isFixedRot();
            bodyDef.userData = rBody.gameObject;
            bodyDef.bullet = rBody.isContinousCol();
            bodyDef.gravityScale = rBody.getGravityScale();
            bodyDef.angularVelocity = rBody.getAngularVelocity();

            switch (rBody.getBodyType()) {
                case Kinematic:
                    bodyDef.type = BodyType.KINEMATIC;
                    break;
                case Static:
                    bodyDef.type = BodyType.STATIC;
                    break;
                case Dynamic:
                    bodyDef.type = BodyType.DYNAMIC;
                    break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rBody.getMass();
            rBody.setBody(body);

            CircleCollider circleCollider;
            BoxCollider boxCollider;
            PillBoxCollider pillBoxCollider;

            if ((circleCollider = gameObject.getComponent(CircleCollider.class)) != null) {
                addCircleCollider(rBody, circleCollider);
            }
            if ((boxCollider = gameObject.getComponent(BoxCollider.class)) != null) {
                addBoxCollider(rBody, boxCollider);
            }
            if ((pillBoxCollider = gameObject.getComponent(PillBoxCollider.class)) != null) {
                addPillCollider(rBody, pillBoxCollider);
            }
        }
    }

    public void addBoxCollider(RBody rBody, BoxCollider boxCollider) {
        Body body = rBody.getBody();
        assert body != null : "body must not be null";
        PolygonShape polygonShape = new PolygonShape();
        Vector2f size = new Vector2f(boxCollider.getSize()).mul(0.5f);
        Vector2f off = boxCollider.getOff();
        polygonShape.setAsBox(size.x, size.y, new Vec2(off.x, off.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1;
        fixtureDef.friction = rBody.getFriction();
        fixtureDef.userData = boxCollider.gameObject;
        fixtureDef.isSensor = rBody.isSensor();
        body.createFixture(fixtureDef);
    }

    public void addCircleCollider(RBody rBody, CircleCollider circleCollider) {
        Body body = rBody.getBody();
        assert body != null : "body must not be null";
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circleCollider.getRadius());
        circleShape.m_p.set(circleCollider.getOff().x, circleCollider.getOff().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1;
        fixtureDef.friction = rBody.getFriction();
        fixtureDef.userData = circleCollider.gameObject;
        fixtureDef.isSensor = rBody.isSensor();
        body.createFixture(fixtureDef);
    }

    public void resetBox2DCollider(RBody rBody, BoxCollider boxCollider) {
        Body body = rBody.getBody();
        if (body == null) {
            return;
        }

        int size = fixtureListSize(body);

        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addBoxCollider(rBody, boxCollider);
        body.resetMassData();
    }

    public void resetCircleCollider(RBody rBody, CircleCollider circleCollider) {
        Body body = rBody.getBody();
        if (body == null) {
            return;
        }

        int size = fixtureListSize(body);

        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addCircleCollider(rBody, circleCollider);
        body.resetMassData();
    }

    public void resetPillCollider(RBody rBody, PillBoxCollider pillBoxCollider) {
        Body body = rBody.getBody();
        if (body == null) {
            return;
        }

        int size = fixtureListSize(body);

        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addPillCollider(rBody, pillBoxCollider);
        body.resetMassData();
    }

    public void addPillCollider(RBody rBody, PillBoxCollider pillBoxCollider) {
        Body body = rBody.getBody();
        assert body != null : "body must not be null";

        addBoxCollider(rBody, pillBoxCollider.getBoxCollider());
        addCircleCollider(rBody, pillBoxCollider.getTop());
        addCircleCollider(rBody, pillBoxCollider.getBot());
    }

    public RayCastInfo RayCast(GameObject obj, Vector2f p1, Vector2f p2) {
        RayCastInfo callback = new RayCastInfo(obj);
        world.raycast(callback, new Vec2(p1.x, p1.y), new Vec2(p2.x, p2.y));
        return callback;
    }

    public void setIsSensor(RBody rBody) {
        Body body = rBody.getBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = true;
            fixture = fixture.m_next;
        }
    }

    public void setNotSensor(RBody rBody) {
        Body body = rBody.getBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = false;
            fixture = fixture.m_next;
        }
    }

    public boolean isLocked() {
        return world.isLocked();
    }

    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        return size;
    }

    public Vector2f getGravity() {
        return new Vector2f(this.world.getGravity().x, this.world.getGravity().y);
    }
}
