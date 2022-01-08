package app.ui.panes;

import app.ui.GUI;
import app.ui.GuiUtils;
import ca.Automaton;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;

public class EditingPane {

    private final VerticalGroup group;
    private Automaton<?> automaton;

    public EditingPane(GUI gui) {
        group = new VerticalGroup();

        ImageButton clearButton = new ImageButton(GuiUtils.getSprite(GUI.atlas, "eraser"));
        clearButton.addListener(new ClickListener(0) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialogs.ConfirmDialog<Boolean> confirmDialog = new Dialogs.ConfirmDialog<>(
                        "Confirm clearing",
                        "Are you sure you want to clear this generation?",
                        new String[]{"Yes", "Cancel"},
                        new Boolean[]{true, false},
                        result -> {
                            if (automaton != null && result) automaton.resetCurrent();
                        }
                );
                confirmDialog.show(event.getStage());
            }
        });

        group.addActor(clearButton);
    }

    public void setAutomaton(Automaton<?> automaton) {
        if (automaton != null) {
            this.automaton = automaton;
            group.setTouchable(Touchable.enabled);
        } else {
            group.setTouchable(Touchable.disabled);
        }
    }

    public Actor getPane() {
        return group;
    }

}
