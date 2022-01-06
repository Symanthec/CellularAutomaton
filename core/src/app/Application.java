package app;

import app.rendering.FrameManager;
import app.rendering.LatticeRenderer2D;
import app.rendering.Renderer;
import app.rendering.ValuePalette;
import app.ui.GUI;
import ca.Automaton;
import ca.PythonLoader;
import ca.rules.LifeRule;
import ca.rules.Rule;
import ca.values.Binary;
import ca.values.Value;
import ca.world.World2D;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static ca.values.Binary.OFF;

public class Application extends Game {

    private Automaton automaton;
    private OrthographicCamera camera;
    private static EvolutionThread<Binary> evoThread;

    private FrameManager frameManager;
    private GUI gui;

    public static long getEvolveCount() {
        return evoThread.getEvolveCount();
    }

    public static void setEvolveCount(long newCount) {
        evoThread.setEvolveCount(newCount);
    }

    @Override
    public void create() {
        // Renderer
        renderBatch = new SpriteBatch();
        Color[] colors = new Color[]{
                Color.BLACK,
                Color.WHITE,
                Color.BLUE,
                Color.RED
        };
        Renderer<Binary> renderer2D = new LatticeRenderer2D<>(new ValuePalette<>(new Binary().getValues(), colors));
        frameManager = new FrameManager(renderer2D);

        final int worldWidth = 100, worldHeight = 100;

        PythonLoader loader = new PythonLoader("binary.py");
        Rule[] rules = loader.getRules();

        Class<Value> val = rules[0].supportedCells();
        System.out.println(val.getSimpleName());

        World2D<Binary> world = new World2D<>(OFF, worldWidth, worldHeight);
        automaton = new Automaton<>(new LifeRule());
        automaton.setRoot(world);
        automaton.addListener(frameManager);
        automaton.setSaveAll(true);

        // Second thread for evolution
        evoThread = new EvolutionThread<>(automaton);
        evoThread.start();

        // User interface
        Stage stage = new Stage(new ScreenViewport());
        gui = new GUI(stage, automaton);
        setScreen(gui);

        camera = gui.getCamera();
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
    }

    private SpriteBatch renderBatch;

    @Override
    public void render() {
        // Render clear
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.20f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // TODO: 27.12.2021 add smooth animation

        // Frame updating
        frameManager.processEvents();

        // Render frame
        Texture tex = frameManager.getFrameOrRender(automaton.at(), automaton.current());
        renderBatch.begin();
        renderBatch.setTransformMatrix(camera.view);
        renderBatch.setProjectionMatrix(camera.projection);
        renderBatch.draw(tex, 0, 0);
        renderBatch.end();

        // event handling is also made in gui render step
        screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        evoThread.killThread();
        frameManager.dispose();
        gui.dispose();
    }
}

class EvolutionThread<V extends Value> extends Thread {

    private final AtomicLong evolveCount;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Automaton<V> automaton;

    public long getEvolveCount() {
        return evolveCount.get();
    }

    public void setEvolveCount(long count) {
        evolveCount.set(count);
    }

    public EvolutionThread(Automaton<V> automaton) {
        this(automaton, 0L);
    }

    public EvolutionThread(Automaton<V> automaton, long evolveCount) {
        this.automaton = automaton;
        this.evolveCount = new AtomicLong(evolveCount);
    }

    public void killThread() {
        running.set(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            if (evolveCount.get() != 0) {
                evolveCount.decrementAndGet();
                automaton.evolve();
            }
        }
    }
}