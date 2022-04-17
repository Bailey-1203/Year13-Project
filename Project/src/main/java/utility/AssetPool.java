package utility;

import components.SpriteSheet;
import iso.Sound;
import late.Shader;
import late.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static final Map<String, Shader> shaders = new HashMap<>();
    private static final Map<String, Texture> Textures = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheet = new HashMap<>();
    private static final Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String Path) {
        File file = new File(Path);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(Path);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }


    public static Texture getTexture(String Path) {
        File file = new File(Path);
        if (AssetPool.Textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.Textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(Path);
            AssetPool.Textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String Name, SpriteSheet spriteSheet) {
        File file = new File(Name);
        if (!AssetPool.spriteSheet.containsKey(file.getAbsolutePath())) {
            AssetPool.spriteSheet.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String Name) {
        File file = new File(Name);
        assert AssetPool.spriteSheet.containsKey(file.getAbsolutePath()) : "error tried to access spritesheet";
        return AssetPool.spriteSheet.getOrDefault(file.getAbsolutePath(), null);
    }

    public static Sound getSound(String Name) {
        File file = new File(Name);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            assert false : "Sound File Not Loaded" + Name;
        }
        return null;
    }

    public static Sound addSound(String Name, boolean loops) {
        File file = new File(Name);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(Name, loops);
            AssetPool.sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }

    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }
}

