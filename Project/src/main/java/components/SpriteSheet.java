package components;

import late.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private final List<Sprite> sprites;

    public SpriteSheet(Texture texture, int S_Width, int S_Height, int S_Separation, int S_Count) {
        this.sprites = new ArrayList<>();

        int curr_x = 0;
        int curr_y = texture.getHeight()-S_Height;

        for (int i=0; i < S_Count; i++) {
            float Ytop = (curr_y+S_Height)/(float) texture.getHeight();
            float Xrig = (curr_x+S_Width)/(float) texture.getWidth();
            float Xlef = curr_x/(float) texture.getWidth();
            float Ybot = curr_y/(float) texture.getHeight();

            Vector2f[] tex_ver = {
                    new Vector2f(Xrig,Ytop),
                    new Vector2f(Xrig,Ybot),
                    new Vector2f(Xlef,Ybot),
                    new Vector2f(Xlef,Ytop)
            };

            Sprite sprite = new Sprite();
            sprite.set_tex(texture);
            sprite.set_pts(tex_ver);
            sprite.setHeight(S_Height);
            sprite.setWidth(S_Width);
            this.sprites.add(sprite);

            curr_x += S_Width + S_Separation;
            if (curr_x >= texture.getWidth()) {
                curr_x = 0;
                curr_y -= S_Height + S_Separation;
            }
        }
    }

    public Sprite getsprite(int i) {
        return this.sprites.get(i);
    }

    public int size() {
        return sprites.size();
    }
}
