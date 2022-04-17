package iso;

import components.*;
import org.joml.Vector2f;
import utility.AssetPool;

public class Prefabs {

    public static GameObject genObjectClicked(Sprite spr, float Width, float Height, int Zindex) {
        GameObject obj = Window.getScene().CreateObj("gen_object");
        obj.transform.Sca = new Vector2f(Width, Height);
        obj.transform.Zindex = Zindex;
        SpriteRenderer rend = new SpriteRenderer();
        rend.setSprite(spr);
        rend.setDisplayImGui(true);
        obj.transform.setDisplayImGui(true);
        obj.addComponent(rend);
        obj.transform.Rotation = 360;

        return obj;
    }

    public static GameObject genSprite() {
        SpriteSheet spriteSheet = AssetPool.getSpriteSheet("assets/textures/spr.png");
        GameObject obj = genObjectClicked(spriteSheet.getsprite(0), 0.25f, 0.25f, Window.getImGuilayer().getEditorControl().getVindex());

        SpriteLoop loop = new SpriteLoop();
        loop.tag = "animation";
        loop.addSprite(spriteSheet.getsprite(8), 0.23f);
        loop.addSprite(spriteSheet.getsprite(9), 0.23f);
        loop.addSprite(spriteSheet.getsprite(10), 0.23f);
        loop.addSprite(spriteSheet.getsprite(11), 0.23f);
        loop.addSprite(spriteSheet.getsprite(12), 0.23f);
        loop.addSprite(spriteSheet.getsprite(13), 0.23f);
        loop.addSprite(spriteSheet.getsprite(14), 0.23f);
        loop.addSprite(spriteSheet.getsprite(15), 0.23f);
        loop.setLoops(true);

        SpriteState spriteState = new SpriteState();
        spriteState.addLoop(loop);
        spriteState.setDefaultTag(loop.tag);
        spriteState.setDisplayImGui(true);
        obj.addComponent(spriteState);

        return obj;
    }
}
