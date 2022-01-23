package app.rendering;

import ca.values.Cell;
import ca.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import static com.badlogic.gdx.graphics.GL20.GL_FLOAT;
import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;

public class LatticeRenderer2D implements Renderer {

    private Palette palette;
    private final FrameBuffer fbo;

    private final OrthographicCamera cam = new OrthographicCamera();
    private final ShaderProgram program = new ShaderProgram(new FileHandle("shaders/lattice_vertex.glsl"), new FileHandle("shaders/lattice_fragment.glsl"));
    private final Mesh square = new Mesh(true, 4, 6,
            new VertexAttribute(0, 2, "aPos", GL_FLOAT)
    );

    public LatticeRenderer2D(Class<? extends Cell> valueClass) {
        this(new RandomPalette(valueClass, Color.BLACK));
    }

    public LatticeRenderer2D(Palette palette) {
        this.palette = palette;

        float[] vertices = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        short[] indices = new short[]{0, 1, 2, 0, 3, 2};
        square.setVertices(vertices);
        square.setIndices(indices);

        fbo = new FrameBuffer();
    }

    @Override
    public Texture render(World world) {
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

    public void redrawCell(Texture output, World world, int... cellPosition) {
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

    @Override
    public Palette getPalette() {
        return palette;
    }

}

