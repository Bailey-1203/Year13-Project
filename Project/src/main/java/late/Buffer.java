package late;

import static org.lwjgl.opengl.GL30.*;

public class Buffer {

    private final int fboID;

    private final Texture tex;

    public Buffer(int Width, int Height){
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        this.tex = new Texture(Width,Height);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.tex.getTexID(),0);

        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, Width, Height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Error: Framebuffer incomplete";

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    public int getFboID() {
        return fboID;
    }

    public int getTexID() {
        return tex.getTexID();
    }

    public void Bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void UnBind() {
        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }
}
