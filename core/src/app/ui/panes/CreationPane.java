package app.ui.panes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

public class CreationPane {

    private final VisWindow window;

    public CreationPane() {
        window = new VisWindow("Automaton", false);
        window.setMovable(false);
        window.setResizable(false);

        VisTextButton createButton = new VisTextButton("New");
        window.add(createButton);
    }

    public Actor getPane() {
        return window;
    }

}
