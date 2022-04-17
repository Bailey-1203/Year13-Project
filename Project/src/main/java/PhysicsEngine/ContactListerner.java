package PhysicsEngine;


import components.Component;
import iso.GameObject;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class ContactListerner implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        GameObject obj1 = (GameObject)contact.getFixtureA().getUserData();
        GameObject obj2 = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f Normal1 = new Vector2f(worldManifold.normal.x,worldManifold.normal.y);
        Vector2f Normal2 = new Vector2f(Normal1).negate();

        for (Component c: obj1.getComponents()) {
            c.BeginCollision(obj2,contact,Normal1);
        }

        for (Component c: obj2.getComponents()) {
            c.BeginCollision(obj1,contact,Normal2);
        }
    }

    @Override
    public void endContact(Contact contact) {
        GameObject obj1 = (GameObject)contact.getFixtureA().getUserData();
        GameObject obj2 = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f Normal1 = new Vector2f(worldManifold.normal.x,worldManifold.normal.y);
        Vector2f Normal2 = new Vector2f(Normal1).negate();

        for (Component c: obj1.getComponents()) {
            c.EndCollision(obj2,contact,Normal1);
        }

        for (Component c: obj2.getComponents()) {
            c.EndCollision(obj1,contact,Normal2);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        GameObject obj1 = (GameObject)contact.getFixtureA().getUserData();
        GameObject obj2 = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f Normal1 = new Vector2f(worldManifold.normal.x,worldManifold.normal.y);
        Vector2f Normal2 = new Vector2f(Normal1).negate();

        for (Component c: obj1.getComponents()) {
            c.PreSolve(obj2,contact,Normal1);
        }

        for (Component c: obj2.getComponents()) {
            c.PreSolve(obj1,contact,Normal2);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        GameObject obj1 = (GameObject)contact.getFixtureA().getUserData();
        GameObject obj2 = (GameObject)contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f Normal1 = new Vector2f(worldManifold.normal.x,worldManifold.normal.y);
        Vector2f Normal2 = new Vector2f(Normal1).negate();

        for (Component c: obj1.getComponents()) {
            c.PostSolve(obj2,contact,Normal1);
        }

        for (Component c: obj2.getComponents()) {
            c.PostSolve(obj1,contact,Normal2);
        }
    }
}
