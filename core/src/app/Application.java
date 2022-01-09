package app;

import app.ui.GUI;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.util.dialog.Dialogs;

import static com.badlogic.gdx.Application.ApplicationType.Desktop;

public class Application extends Game {

    private GUI gui;

    @Override
    public void create() {
        // User interface
        Stage stage = new Stage(new ScreenViewport());
        gui = new GUI(stage);
        setScreen(gui);

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        if (Gdx.app.getType() == Desktop) {
            Gdx.app.setApplicationLogger(new ApplicationLogger() {
                @Override
                public void log(String tag, String message) {
                    System.out.println("[" + tag + "] " + message);
                }

                @Override
                public void log(String tag, String message, Throwable exception) {
                    System.out.println("[" + tag + "] " + message);
                    exception.printStackTrace(System.out);
                }

                @Override
                public void error(String tag, String message) {
                    Dialogs.showErrorDialog(stage, String.format("[%s]: %s", tag, message));
                }

                @Override
                public void error(String tag, String message, Throwable exception) {
                    Dialogs.showErrorDialog(stage, String.format("[%s]: %s", tag, message));
                    exception.printStackTrace(System.err);
                }

                @Override
                public void debug(String tag, String message) {
                    System.out.println("[" + tag + "] " + message);
                }

                @Override
                public void debug(String tag, String message, Throwable exception) {
                    System.out.println("[" + tag + "] " + message);
                    exception.printStackTrace(System.out);
                }
            });
        }
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