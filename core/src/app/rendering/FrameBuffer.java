package app.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

import java.nio.IntBuffer;

import static com.badlogic.gdx.graphics.GL20.*;

public class FrameBuffer implements Disposable {

    private final int fbo;

    public FrameBuffer() {
        fbo = Gdx.gl.glGenFramebuffer();
        bind();
        IntBuffer buffers = BufferUtils.newIntBuffer(1);
        buffers.put(0, GL_COLOR_ATTACHMENT0);
        Gdx.gl30.glDrawBuffers(1, buffers);
        unbind();
    }

    public Texture createTexture(int width, int height) {
        Texture tex = new Texture(width, height, Pixmap.Format.RGBA8888);
        tex.bind();
        Gdx.gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
        Gdx.gl.glBindTexture(GL_TEXTURE_2D, GL_NONE);
        return tex;
    }

    public void useTexture(Texture tex) {
        Gdx.gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tex.getTextureObjectHandle(), 0);
    }

    public void bind() {
        Gdx.gl.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
    }

    public void unbind() {
        Gdx.gl.glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE);
    }

    @Override
    public void dispose() {
        Gdx.gl.glDeleteFramebuffer(fbo);
    }
}
