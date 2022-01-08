package app;

import app.ui.GUI;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Application extends Game {

    private GUI gui;

    @Override
    public void create() {
        // User interface
        Stage stage = new Stage(new ScreenViewport());
        gui = new GUI(stage);
        setScreen(gui);

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
    }

    @Override
    public void render() {
        // event handling is also made in gui render step
        screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        gui.dispose();
    }
}