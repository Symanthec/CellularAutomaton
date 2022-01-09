package app.ui;

import app.automaton.EvolutionThread;
import app.rendering.FrameManager;
import app.rendering.Renderer;
import app.ui.panes.*;
import ca.Automaton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisWindow;

@SuppressWarnings("rawtypes")
public class GUI extends ScreenAdapter {

    private Automaton automaton;
    private Values values;

    private FrameManager frameManager;
    private EvolutionThread evo;

    private final Stage stage;

    private InputController inputController;
    private OrthographicCamera camera;

    private EvolvingPane evolvePane;
    private EditingPane editPane;

    private SpriteBatch batch;

    public static TextureAtlas atlas;

    public GUI(Stage stage) {
        this.stage = stage;
        atlas = new TextureAtlas("atlases/icons.pack");
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        evo = new EvolutionThread(null);
        evo.start();

        VisUI.load();
        inputController = new InputController(this, camera);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputController));

        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // panels
        VisWindow window = new VisWindow("", false);
        window.padTop(window.getPadBottom());
        window.setMovable(false);
        window.setResizable(false);
        HorizontalGroup paneGroup = new HorizontalGroup();
        window.add(paneGroup);

        evolvePane = new EvolvingPane(this);
        editPane = new EditingPane(this);

        paneGroup.addActor(new CreationPane(this).getPane());
        paneGroup.addActor(evolvePane.getPane());
        paneGroup.addActor(editPane.getPane());
        paneGroup.addActor(new ExportPane(this).getPane());
        root.add(window).left().row();
        root.add().expand().fill();

        // lose keyboard focus when clicked not on TextField
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        // input handling
        stage.act(delta);
        inputController.update();

        // Render clear
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.20f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // frame rendering
        if (frameManager != null && automaton != null) {
            frameManager.processEvents();

            // Render frame
            Texture tex = frameManager.getFrameOrRender(automaton.at(), automaton.current());
            batch.begin();
            batch.setTransformMatrix(camera.view);
            batch.setProjectionMatrix(camera.projection);
            batch.draw(tex, 0, 0);
            batch.end();
        }

        stage.draw();
//        stage.setDebugAll(true);
    }

    // to work GUI needs FrameManager and Automaton
    public void reload(Automaton automaton, Renderer<?> renderer, Values values) {
        this.automaton = automaton;
        if (renderer != null && automaton != null) {
            if (frameManager != null)
                frameManager.reset();
            frameManager = new FrameManager(renderer);
            automaton.addListener(frameManager);
        }
        inputController.setAutomaton(automaton, values);
        evolvePane.setAutomaton(automaton);
        editPane.setAutomaton(automaton, renderer, values);
        evo.setAutomaton(automaton);
        this.values = values;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        Gdx.gl.glViewport(0, 0, width, height);
        camera.setToOrtho(false, width, height);
        if (automaton != null)
            camera.position.set(automaton.last().getBounds()[0] / 2f, automaton.last().getBounds()[1] / 2f, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        evo.killThread();
    }

    public Automaton getAutomaton() {
        return automaton;
    }

    public EvolutionThread getEvolver() {
        return evo;
    }

    public FrameManager getFrameManager() {
        return frameManager;
    }

    public Values getValues() {
        return values;
    }
}
