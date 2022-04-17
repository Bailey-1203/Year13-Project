package late;

import iso.Window;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private transient int texture_ID;

    private int width,height;

    private String FilePath;

    public Texture() {
        texture_ID = -1;
        width = -1;
        height = -1;
    }

    public Texture(int Width, int Height){

        this.FilePath = "Generated";

        texture_ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture_ID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Width, Height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

    public void init(String filepath) {

        this.FilePath = filepath;
        File file = new File(filepath);
        if (!file.exists()) {
            file = new File(Window.getRootFolder()+"//assets//"+file.getName());
            FilePath = file.getAbsolutePath();
            assert file.exists() : "Image File '"+ filepath+"' does not exist.";
        }

        texture_ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture_ID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        IntBuffer  tex_width  = BufferUtils.createIntBuffer(1);
        IntBuffer  tex_height = BufferUtils.createIntBuffer(1);
        IntBuffer  channels   = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);

        ByteBuffer image      = stbi_load(FilePath, tex_width, tex_height, channels, 0);

        if (image != null) {
            this.width = tex_width.get(0);
            this.height = tex_height.get(0);
        }

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, tex_width.get(0),
                        tex_height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tex_width.get(0),
                        tex_height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else {
                assert false : "Error: unknown channel width of texture. '" + filepath + "'";
            }
        }
        else {
            assert false : "Error: Could not load image. '" + filepath + "'";
        }

        stbi_image_free(image);

    }

    public void Bind() {
        glBindTexture(GL_TEXTURE_2D, texture_ID);
    }

    public void Unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexID() {
        return this.texture_ID;
    }

    public String getFilePath(){return this.FilePath;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null){return false;}
        if (!(obj instanceof Texture)){return false;}
        Texture objTex = (Texture)obj;
        return objTex.getWidth() == this.width &&
               objTex.getHeight() == this.getHeight() &&
               objTex.getTexID() == this.texture_ID &&
               objTex.getFilePath().equals(this.FilePath);
    }
}
