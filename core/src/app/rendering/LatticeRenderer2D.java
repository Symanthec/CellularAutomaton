package app.rendering;

import ca.values.Value;
import ca.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

import java.nio.IntBuffer;

import static com.badlogic.gdx.graphics.GL20.*;

public class LatticeRenderer2D<V extends Value> implements Renderer<V> {

    private Palette palette;

    private FrameBuffer fbo;

    private final OrthographicCamera cam = new OrthographicCamera();
    private final ShaderProgram program = new ShaderProgram(new FileHandle("shaders/lattice_vertex.glsl"), new FileHandle("shaders/lattice_fragment.glsl"));
    private final Mesh square = new Mesh(true, 4, 6,
            new VertexAttribute(0, 2, "aPos", GL_FLOAT)
    );

    public LatticeRenderer2D(Class<V> valueClass) {
        this(new RandomPalette<>(valueClass, Color.BLACK));
    }

    public LatticeRenderer2D(RandomPalette<V> palette) {
        this.palette = palette;

        float[] vertices = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        short[] indices = new short[]{0, 1, 2, 0, 3, 2};
        square.setVertices(vertices);
        square.setIndices(indices);

        fbo = new FrameBuffer();
    }

    @Override
    public Texture render(World<V> world) {
        int width = world.getBounds()[0], height = world.getBounds()[1];

        fbo.bind();
        Texture tex = fbo.createTexture(width, height);
        fbo.useTexture(tex);

        Gdx.gl.glViewport(0, 0, width, height);
        cam.setToOrtho(true, width, height);
        program.bind();
        program.setUniformMatrix4fv("view", cam.combined.getValues(), 0, 16);

        for (int[] pos : world) {
            Color col = palette.getColorFor(world.getCell(pos));
            program.setUniform4fv("cellColor", getCol(col), 0, 4);
            Matrix4 model = new Matrix4().translate(pos[0], pos[1], 0);
            program.setUniformMatrix4fv("model", model.getValues(), 0, 16);
            square.render(program, GL_TRIANGLES);
        }

        fbo.unbind();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return tex;
    }

    private float[] getCol(Color color) {
        return new float[]{color.r, color.g, color.b, color.a};
    }

    @Override
    public void dispose() {
        square.dispose();
        program.dispose();
        fbo.dispose();
    }

    public void redrawCell(Texture output, World<V> world, int... cellPosition) {
        fbo.bind();
        fbo.useTexture(output);

        Gdx.gl.glViewport(0, 0, output.getWidth(), output.getHeight());
        cam.setToOrtho(true, output.getWidth(), output.getHeight());
        program.bind();
        program.setUniformMatrix4fv("view", cam.combined.getValues(), 0, 16);

        Color col = palette.getColorFor(world.getCell(cellPosition));
        Matrix4 model = new Matrix4().translate(cellPosition[0], cellPosition[1], 0);
        program.setUniform4fv("cellColor", getCol(col), 0, 4);
        program.setUniformMatrix4fv("model", model.getValues(), 0, 16);

        square.render(program, GL_TRIANGLES);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fbo.unbind();
    }

    @Override
    public void setPalette(Palette palette) {
        this.palette = palette;
    }

}

class FrameBuffer implements Disposable {

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
