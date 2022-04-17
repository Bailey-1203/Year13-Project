package components;

import GameEditor.MImGui;
import iso.Transform;
import late.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private final Vector4f originColour = new Vector4f(1, 1, 1, 1);
    private Vector4f Colour = new Vector4f(1, 1, 1, 1);
    private float rotation_pre;

    private Sprite sprite = new Sprite();

    private transient boolean need_reload = true;

    private transient Transform transform_prev;

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public void setTexture(Texture texture) {
        this.sprite.set_tex(texture);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.need_reload = true;
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTex_ver();
    }

    @Override
    public void start() {
        this.transform_prev = gameObject.transform.dupe();
    }

    public boolean Need_reload() {
        return this.need_reload;
    }

    public void Updated() {
        this.need_reload = false;
    }

    @Override
    public void EditorUpdate(float delta_time) {
        if (!this.transform_prev.eq(this.gameObject.transform)) {
            this.gameObject.transform.dupe(this.transform_prev);
            need_reload = true;
        } else if (this.rotation_pre != this.gameObject.transform.Rotation) {
            this.rotation_pre = this.gameObject.transform.Rotation;
            need_reload = true;
        }
    }

    @Override
    public void imgui() {
        if (MImGui.drawColourPickerV4f("Colour Picker", Colour)) {
            this.need_reload = true;
        }
    }

    public Vector4f getColour() {
        return this.Colour;
    }

    public void setColour(Vector4f Colour) {
        if (!(this.Colour.equals(Colour)))
            this.need_reload = true;
        this.Colour = Colour;
    }

    public Vector4f getOriginColour() {
        return this.originColour;
    }

    public void needReload() {
        this.need_reload = true;
    }
}
