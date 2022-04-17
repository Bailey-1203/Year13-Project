package components;

import utility.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class SpriteLoop {
    public static Sprite sprite = new Sprite();
    public String tag;
    public List<Frame> Sprites = new ArrayList<>();
    private transient float timer = 0.0f;
    private transient int current = 0;
    private boolean loops;

    public void addSprite(Sprite sprite, float timer) {
        Sprites.add(new Frame(sprite, timer));
    }

    public void refreshTextures() {
        for (Frame frame : Sprites) {
            frame.sprite.set_tex(AssetPool.getTexture(frame.sprite.getTexture().getFilePath()));
        }
    }

    public boolean getLoops() {
        return loops;
    }

    public void setLoops(boolean a) {
        this.loops = a;
    }

    public void update(float dt) {
        if (current < Sprites.size()) {
            timer -= dt;
            if (timer < 0) {
                current = (current + 1) % Sprites.size();
                timer = Sprites.get(current).timer;
            }
        }
    }

    public Sprite getSprite() {
        if (current < Sprites.size()) {
            return Sprites.get(current).sprite;
        } else {
            return sprite;
        }
    }

    public List<Frame> getSprites() {
        return this.Sprites;
    }

    public String getTag() {
        return tag;
    }
}
