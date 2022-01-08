package app.ui.panes;

import app.ui.GUI;
import app.ui.GuiUtils;
import ca.PythonLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;

public class CreationPane {

    private final VerticalGroup group;

    public CreationPane(GUI gui) {
        group = new VerticalGroup();
        group.padRight(50);
        group.addActor(new VisLabel("Automaton"));

        HorizontalGroup buttonGroup = new HorizontalGroup();
        VerticalGroup addDelGroup = new VerticalGroup();

        ImageButton createButton = new ImageButton(GuiUtils.getSprite(GUI.atlas, "create"));
        Tooltip<Label> createTooltip = GuiUtils.getTooltip("Create new automaton");
        createButton.addListener(createTooltip);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog creationWindow = new CreateAutomatonWindow(gui, "Create new automaton");
                creationWindow.show(event.getStage());
            }
        });

        ImageButton deleteButton = new ImageButton(GuiUtils.getSprite(GUI.atlas, "delete"));
        Tooltip<Label> deleteTooltip = GuiUtils.getTooltip("Delete automaton");
        deleteButton.addListener(deleteTooltip);
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialogs.ConfirmDialog<Boolean> confirmDialog = new Dialogs.ConfirmDialog<>(
                        "Confirm clearing",
                        "Are you sure you want to delete cellular automaton?",
                        new String[]{"Yes", "Cancel"},
                        new Boolean[]{true, false},
                        result -> {
                            if (gui.getAutomaton() != null && result) gui.reload(null, null);
                        }
                );
                confirmDialog.show(event.getStage());
            }
        });

        ImageButton jython = new ImageButton(GuiUtils.getSprite(GUI.atlas, "python"));
        jython.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialogs.showInputDialog(
                    event.getStage(),
                    "Enter path",
                    "Enter python file path:",
                    true,
                    new InputDialogAdapter() {
                        @Override
                        public void finished(String input) {
                            PythonLoader loader = new PythonLoader(input);
                            loader.loadRules();
                            loader.loadValues();
                        }
                    });
            }
        });

        addDelGroup.addActor(createButton);
        addDelGroup.addActor(deleteButton);
        buttonGroup.addActor(addDelGroup);

        buttonGroup.addActor(jython);

        group.addActor(buttonGroup);
    }

    public Actor getPane() {
        return group;
    }

}
