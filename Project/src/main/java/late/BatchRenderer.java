package late;

import components.SpriteRenderer;
import iso.GameObject;
import iso.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BatchRenderer implements Comparable<BatchRenderer> {

    private final int Pos_Size = 2;
    private final int Col_Size = 4;
    private final int Tex_Cood = 2;
    private final int Tex_ID_S = 1;
    private final int Obj_ID_S = 1;

    private final int Pos_Off = 0;
    private final int Byte_S = Float.BYTES;
    private final int Col_Off = Pos_Off + Pos_Size * Byte_S;
    private final int Tex_Off = Col_Off + Col_Size * Byte_S;
    private final int Tex_ID_O = Tex_Off + Tex_Cood * Byte_S;
    private final int Obj_ID_O = Tex_ID_O + Tex_ID_S * Byte_S;
    private final int Vtx_Size = 10;
    private final int Vtx_Byte = Vtx_Size * Byte_S;

    private final SpriteRenderer[] sprites;
    private final int Max_Batch;
    private final int Z_ind;
    private final int[] TexSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private final float[] vertices;
    private final List<Texture> textures;
    private final Renderer renderer;
    private int Sprites_count;
    private int Vao_ID, Vbo_ID;
    private boolean has_Room;

    public BatchRenderer(int max_Batch, int i, Renderer renderer) {

        this.sprites = new SpriteRenderer[max_Batch];
        this.Max_Batch = max_Batch;
        this.Z_ind = i;
        this.renderer = renderer;

        vertices = new float[Max_Batch * Vtx_Size * 4];

        this.Sprites_count = 0;
        this.has_Room = true;
        this.textures = new ArrayList<>();
    }

    public void addSprite(SpriteRenderer sprite) {
        int Index = this.Sprites_count;
        this.sprites[Index] = sprite;
        this.Sprites_count++;

        if (sprite.getTexture() != null) {
            if (!textures.contains(sprite.getTexture())) {
                textures.add(sprite.getTexture());
            }
        }

        loadVtxProp(Index);

        if (Sprites_count >= this.Max_Batch) {
            this.has_Room = false;
        }
    }

    private void loadVtxProp(int i) {
        SpriteRenderer sprite = this.sprites[i];

        int offset = i * 4 * Vtx_Size;

        Vector4f colour = sprite.getColour();
        Vector2f[] TexCoods = sprite.getTexCoords();

        int TexID = 0;
        if (sprite.getTexture() != null) {
            for (int j = 0; j < textures.size(); j++) {
                if (textures.get(j).equals(sprite.getTexture())) {
                    TexID = j + 1;
                    break;
                }
            }
        }

        boolean Rotated = sprite.gameObject.transform.Rotation != 0;
        Matrix4f transform = new Matrix4f().identity();

        if (Rotated) {
            transform.translate(sprite.gameObject.transform.Pos.x, sprite.gameObject.transform.Pos.y, 0);
            transform.rotate((float) Math.toRadians(sprite.gameObject.transform.Rotation), 0, 0, 1);
            transform.scale(sprite.gameObject.transform.Sca.x, sprite.gameObject.transform.Sca.y, 1);
        }

        float xAdd = 0.5f;
        float yAdd = 0.5f;

        for (int k = 0; k < 4; k++) {
            if (k == 1) {
                yAdd = -0.5f;
            } else if (k == 2) {
                xAdd = -0.5f;
            } else if (k == 3) {
                yAdd = 0.5f;
            }

            Vector4f CurrPos = new Vector4f(sprite.gameObject.transform.Pos.x + (xAdd + sprite.gameObject.transform.Sca.x),
                    sprite.gameObject.transform.Pos.y + (yAdd * sprite.gameObject.transform.Sca.y),
                    0, 1);

            if (Rotated) {
                CurrPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transform);
            }
            vertices[offset] = CurrPos.x;
            vertices[offset + 1] = CurrPos.y;

            vertices[offset + 2] = colour.x;
            vertices[offset + 3] = colour.y;
            vertices[offset + 4] = colour.z;
            vertices[offset + 5] = colour.w;

            vertices[offset + 6] = TexCoods[k].x;
            vertices[offset + 7] = TexCoods[k].y;

            vertices[offset + 8] = TexID;

            vertices[offset + 9] = sprite.gameObject.getUnivID() + 1;

            offset += Vtx_Size;
        }
    }

    public boolean HasRoom() {
        return this.has_Room;
    }

    public void start() {

        Vao_ID = glGenVertexArrays();
        glBindVertexArray(Vao_ID);

        Vbo_ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, Vbo_ID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int Ebo_ID = glGenBuffers();
        int[] indices = GenIndices();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Ebo_ID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, Pos_Size, GL_FLOAT, false, Vtx_Byte, Pos_Off);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, Col_Size, GL_FLOAT, false, Vtx_Byte, Col_Off);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, Tex_Cood, GL_FLOAT, false, Vtx_Byte, Tex_Off);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, Tex_ID_S, GL_FLOAT, false, Vtx_Byte, Tex_ID_O);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, Obj_ID_S, GL_FLOAT, false, Vtx_Byte, Obj_ID_O);
        glEnableVertexAttribArray(4);
    }

    public void renderTriangles() {
        boolean rebuffer = false;
        for (int i = 0; i < Sprites_count; i++) {
            SpriteRenderer sprite = sprites[i];
            if (sprite.Need_reload()) {
                loadVtxProp(i);
                sprite.Updated();
                rebuffer = true;
            }

            if (sprite.gameObject.transform.Zindex != this.Z_ind) {
                destroy(sprite.gameObject);
                this.renderer.add(sprite.gameObject);
                i--;
            }
        }

        if (rebuffer) {
            glBindBuffer(GL_ARRAY_BUFFER, Vbo_ID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        Shader shader = Renderer.getShader();

        shader.uploadMat4f("uProj", Window.getScene().camera().getproj_mat());
        shader.uploadMat4f("uView", Window.getScene().camera().getview_mat());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).Bind();
        }
        shader.uploadIntArray("uTextures", TexSlots);

        glBindVertexArray(Vao_ID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.Sprites_count * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (Texture texture : textures) {
            texture.Unbind();
        }

        shader.detach();
    }

    private int[] GenIndices() {
        int[] elements = new int[6 * Max_Batch];
        for (int i = 0; i < Max_Batch; i++) {
            LoadElmIndices(elements, i);
        }

        return elements;
    }

    private void LoadElmIndices(int[] elements, int Index) {
        int offsetIndex = 6 * Index;
        int offset = 4 * Index;

        elements[offsetIndex] = offset + 3;
        elements[offsetIndex + 1] = offset + 2;
        elements[offsetIndex + 2] = offset;

        elements[offsetIndex + 3] = offset;
        elements[offsetIndex + 4] = offset + 2;
        elements[offsetIndex + 5] = offset + 1;
    }

    public boolean TextureRoom() {
        return (this.textures.size() < 8);
    }

    public boolean HasTexture(Texture texture) {
        return (this.textures.contains(texture));
    }

    public int getZ_index() {
        return this.Z_ind;
    }

    @Override
    public int compareTo(BatchRenderer batchRenderer) {
        return Integer.compare(this.Z_ind, batchRenderer.Z_ind);
    }

    public boolean destroy(GameObject gameObject) {
        SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
        for (int i = 0; i < Sprites_count; i++) {
            if (sprites[i] == spr) {
                for (int j = i; j < Sprites_count - 1; j++) {
                    sprites[j] = sprites[j + 1];
                    sprites[j].needReload();
                }
                Sprites_count--;
                return true;
            }
        }
        return false;
    }
}
