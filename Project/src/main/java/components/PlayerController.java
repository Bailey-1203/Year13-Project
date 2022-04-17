package components;

import PhysicsEngine.Components.RBody;
import PhysicsEngine.RayCastInfo;
import iso.KeyDetection;
import iso.Window;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    private final Vector2f TerminalVel = new Vector2f(2f, 10f);
    private final transient PlayerPower playerPower = PlayerPower.PowerLvl1;
    private final transient float PlayerWidth = 0.25f;
    private final transient Vector2f acc = new Vector2f();
    private final transient Vector2f vel = new Vector2f();
    private final transient boolean Dead = false;
    private transient boolean GroundContact = false;
    private transient float GroundDeBounce = 0.0f;
    private transient RBody rBody;
    private transient SpriteState spriteState;
    private transient int jumpTime = 0;
    public PlayerController() {

    }

    public void start() {
        this.rBody = gameObject.getComponent(RBody.class);
        this.spriteState = gameObject.getComponent(SpriteState.class);
        this.rBody.setGravityScale(0.0f);
    }

    public void update(float dt) {
        float slowDownForce = 0.05f;
        float runSpeed = 1f;
        if (KeyDetection.IsPressed(GLFW_KEY_RIGHT) || KeyDetection.IsPressed(GLFW_KEY_D)) {
            this.gameObject.transform.Sca.x = PlayerWidth;
            this.acc.x = runSpeed;

            if (this.vel.x < 0) {
                this.spriteState.trigger("ChangeDirection");
                this.vel.x += slowDownForce;
            } else {
                this.spriteState.trigger("BeginRun");
            }
        } else if (KeyDetection.IsPressed(GLFW_KEY_LEFT) || KeyDetection.IsPressed(GLFW_KEY_A)) {
            this.gameObject.transform.Sca.x = -PlayerWidth;
            this.acc.x = -runSpeed;

            if (this.vel.x > 0) {
                this.spriteState.trigger("ChangeDirection");
                this.vel.x -= slowDownForce;
            } else {
                this.spriteState.trigger("BeginRun");
            }
        } else {
            this.acc.x = 0;
            if (this.vel.x > 0) {
                this.vel.x = Math.max(0, this.vel.x - slowDownForce);
            } else if (this.vel.x < 0) {
                this.vel.x = Math.min(0, this.vel.x + slowDownForce);
            }

            if (this.vel.x == 0) {
                this.spriteState.trigger("StopRunning");
            }

        }

        OnGround();
        if (KeyDetection.IsPressed(GLFW_KEY_SPACE) && (jumpTime > 0 || GroundContact || GroundDeBounce > 0)) {
            if ((GroundContact || GroundDeBounce > 0) && jumpTime == 0) {
                jumpTime = 28;
                this.vel.y = 3f;
            } else if (jumpTime > 0) {
                jumpTime--;
                float jumpBoost = 1f;
                this.vel.y = ((jumpTime / 2.2f) * jumpBoost);
            }
            GroundDeBounce = 0;
        } else if (!GroundContact) {
            if (jumpTime > 0) {
                this.vel.y *= 0.35f;
                this.jumpTime = 0;
            }
            GroundDeBounce -= dt;
            this.acc.y = Window.getPhysics().getGravity().y * 0.2f;
        } else {
            this.vel.y = 0;
            this.acc.y = 0;
            GroundDeBounce = 0.1f;
        }

        if (this.vel.y != 0) {
            System.out.println(this.vel.y);
        }

        this.vel.x += this.acc.x * dt;
        this.vel.y += this.acc.y * dt;
        this.vel.x = Math.max(Math.min(this.vel.x, this.TerminalVel.x), -this.TerminalVel.x);
        this.vel.y = Math.max(Math.min(this.vel.y, this.TerminalVel.y), -this.TerminalVel.y);
        this.rBody.setVel(this.vel);
        this.rBody.setAngularVelocity(0);

        if (!GroundContact) {
            spriteState.trigger("Jump");
        } else {
            spriteState.trigger("Idle");
        }
    }

    public void OnGround() {
        Vector2f RayCast = new Vector2f(this.gameObject.transform.Pos);
        float PlayerWidthI = this.PlayerWidth * 0.8f;
        RayCast.sub(PlayerWidthI / 2, 0);
        float YVal = playerPower == PlayerPower.PowerLvl1 ? -0.14f : -0.24f;
        Vector2f RayCastLast = new Vector2f(RayCast).add(0, YVal);

        RayCastInfo rayCastInfo = Window.getPhysics().RayCast(gameObject, RayCast, RayCastLast);

        Vector2f RayCast2 = new Vector2f(RayCast).add(PlayerWidthI, 0);
        Vector2f RayCast2Last = new Vector2f(RayCastLast).add(PlayerWidthI, 0);
        RayCastInfo rayCastInfo2 = Window.getPhysics().RayCast(gameObject, RayCast2, RayCast2Last);

        GroundContact = (rayCastInfo.hit && rayCastInfo.objHit != null) || (rayCastInfo2.hit && rayCastInfo2.objHit != null);
    }

    private enum PlayerPower {
        PowerLvl1,
        PowerLvl2,
        PowerLvl3,
        PowerLvl4
    }

    /*@Override
    public void beginCollision(GameObject gameObject, Contact contact, Vector2f contactNormal) {
        if (Dead) return;

        if (Math.abs(contactNormal.x > 0.8)) {
            this.vel.x = 0;
        }
    }*/
}
