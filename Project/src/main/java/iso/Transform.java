package iso;

import GameEditor.MImGui;
import components.Component;
import org.joml.Vector2f;

public class Transform extends Component {

    public Vector2f Pos;
    public Vector2f Sca;
    public int Zindex;
    public float Rotation = 0.0f;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f Position) {
        init(Position, new Vector2f());
    }

    public Transform(Vector2f Position, Vector2f Scale) {
        init(Position, Scale);
    }

    public void init(Vector2f pos, Vector2f sca) {
        this.Pos = pos;
        this.Sca = sca;
    }

    public Transform dupe() {
        return new Transform(new Vector2f(this.Pos), new Vector2f(this.Sca));
    }

    public void dupe(Transform t) {
        t.Pos.set(this.Pos);
        t.Sca.set(this.Sca);
    }

    public boolean eq(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform) o;
        return t.Pos.equals(this.Pos) && t.Sca.equals(this.Sca);
    }

    @Override
    public void imgui() {
        gameObject.setObjectName(MImGui.drawTextInput("Name:", gameObject.getObjectName()));
        MImGui.drawVec2Control("position", this.Pos, 0, 250, 0.005f);
        MImGui.drawVec2Control("Scale", this.Sca, 1, 250, 0.005f);
        this.Rotation = MImGui.drawFLoatControl("Rotation", this.Rotation);
        this.Zindex = MImGui.drawIntControl("VIndexLayer", this.Zindex);
        MImGui.drawText2Col("Delete :", "Deletes the Current Active Object");
    }

    public Vector2f getPos() {
        return this.Pos;
    }
}
