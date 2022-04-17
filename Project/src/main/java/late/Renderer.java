package late;

import components.SpriteRenderer;
import iso.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final List<BatchRenderer> batches;

    private static Shader shader;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
        if (sprite != null) {
            add(sprite);
        }
    }

    private void add(SpriteRenderer sprite) {
        boolean add = false;
        for (BatchRenderer batch : batches) {
            if (batch.HasRoom() && batch.getZ_index() == sprite.gameObject.transform.Zindex) {
                Texture tex  = sprite.getTexture();
                if (tex == null || (batch.HasTexture(tex) || batch.TextureRoom())) {
                    batch.addSprite(sprite);
                    add = true;
                    break;
                }
            }
        }

        if (!add) {
            int max_Batch = 1000;
            BatchRenderer NewBatch = new BatchRenderer(max_Batch,sprite.gameObject.transform.Zindex, this);
            NewBatch.start();
            batches.add(NewBatch);
            NewBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render() {
        shader.use();
        for (BatchRenderer batchRenderer : batches) {
            batchRenderer.renderTriangles();
        }
    }

    public static void BindShader(Shader shader1) {
        shader = shader1;
    }

    public static Shader getShader() {
        return shader;
    }

    public void removeObject(GameObject gameObject) {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            for (BatchRenderer batch: batches) {
                if (batch.destroy(gameObject)) {
                    return;
                }
            }
        }
    }
}
