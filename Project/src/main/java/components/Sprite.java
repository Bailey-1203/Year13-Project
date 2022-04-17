package components;

import late.Texture;
import org.joml.Vector2f;

public class Sprite {

    private float Width, Height;

    private Texture texture = null;

    private Vector2f[] tex_ver = new Vector2f[]{
            new Vector2f(1,1),
            new Vector2f(1,0),
            new Vector2f(0,0),
            new Vector2f(0,1)
    };

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTex_ver() {
        return this.tex_ver;
    }

    public void set_tex(Texture texture) {
        this.texture = texture;
        this.Width = texture.getWidth();
        this.Height = texture.getHeight();
    }

    public void set_pts(Vector2f[] pts) {
        this.tex_ver = pts;
    }

    public float getWidth() {
        return this.Width;
    }

    public void setWidth(float width) {
        this.Width = width;
    }

    public float getHeight() {
        return this.Height;
    }

    public void setHeight(float height) {
        this.Height = height;
    }

    public int getTexID() {
        return texture == null ? -1 : texture.getTexID();
    }
}
