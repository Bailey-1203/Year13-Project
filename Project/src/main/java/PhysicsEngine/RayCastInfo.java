package PhysicsEngine;

import iso.GameObject;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class RayCastInfo implements RayCastCallback {
    public Fixture fixture;
    public Vector2f Point;
    public Vector2f Normal;
    public float fraction;
    public boolean hit;
    public GameObject objHit;
    private final GameObject object;

    public RayCastInfo(GameObject obj) {
        fixture = null;
        Point = new Vector2f();
        Normal = new Vector2f();
        fraction = 0.0f;
        objHit = null;
        hit = false;
        this.object = obj;
    }

    @Override
    public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        if (fixture.m_userData == object) {
            return 1;
        }
        this.fixture = fixture;
        this.Point = new Vector2f(point.x,point.y);
        this.Normal = new Vector2f(normal.x,normal.y);
        this.hit = fraction != 0;
        this.fraction = fraction;
        this.objHit = (GameObject)fixture.m_userData;
        return fraction;
    }
}
