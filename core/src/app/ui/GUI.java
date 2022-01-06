package app.ui;

import app.ui.panes.CreationPane;
import app.ui.panes.EditingPane;
import app.ui.panes.EvolvingPane;
import ca.Automaton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;

@SuppressWarnings("rawtypes")
public class GUI extends ScreenAdapter {

    private final Automaton automaton;
    private final InputController inputController;
    private final OrthographicCamera camera;
    private final Stage stage;

    public static TextureAtlas atlas;

    public GUI(Stage stage, Automaton automaton) {
        this.stage = stage;
        this.automaton = automaton;
        atlas = new TextureAtlas("atlases/icons.pack");

        camera = new OrthographicCamera();
        inputController = new InputController(camera, automaton);
    }

    @Override
    public void show() {
        VisUI.load();

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputController));

        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // menu
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        file.addItem(new MenuItem("Close"));
        menuBar.addMenu(file);

        root.add(menuBar.getTable()).growX().row();

        // panels
        HorizontalGroup paneGroup = new HorizontalGroup();
        root.add(paneGroup).left().row();
        root.add().expand().fill();

        CreationPane creationPane = new CreationPane();
        paneGroup.addActor(creationPane.getPane());
        EvolvingPane evolvePane = new EvolvingPane(automaton);
        paneGroup.addActor(evolvePane.getPane());
        EditingPane editPane = new EditingPane(automaton);
        paneGroup.addActor(editPane.getPane());

        // lose keyboard focus when clicked not on TextField
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
        inputController.update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        Gdx.gl.glViewport(0, 0, width, height);
        camera.setToOrtho(false, width, height);
        camera.position.set(automaton.last().getBounds()[0] / 2f, automaton.last().getBounds()[1] / 2f, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
