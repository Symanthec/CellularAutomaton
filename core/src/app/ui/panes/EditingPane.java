package app.ui.panes;

import app.ui.GuiUtils;
import app.ui.GUI;
import ca.Automaton;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisWindow;

public class EditingPane {

    private final VisWindow window;
    private final Automaton automaton;

    public EditingPane(Automaton automaton) {
        this.automaton = automaton;
        this.window = new VisWindow("Editing", false);
        window.setMovable(false);
        window.setResizable(false);

        ImageButton clearButton = new ImageButton(GuiUtils.getSprite(GUI.atlas, "clear"));
        clearButton.addListener(new ClickListener(0) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialogs.ConfirmDialog<Boolean> confirmDialog = new Dialogs.ConfirmDialog<>(
                        "Confirm clearing",
                        "Are you sure you want to clear this generation?",
                        new String[]{"Yes", "No"},
                        new Boolean[]{true, false},
                        result -> {
                            if (result) automaton.resetCurrent();
                        }
                );
                confirmDialog.show(event.getStage());
            }
        });

        window.add(clearButton);
    }

    public Actor getPane() {
        return window;
    }

}
