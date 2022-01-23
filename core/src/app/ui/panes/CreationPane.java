package app.ui.panes;

import app.ui.GUI;
import app.ui.GuiUtils;
import ca.PythonLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class CreationPane {

    private final VerticalGroup group;

    public CreationPane(GUI gui) {
        group = new VerticalGroup();
        group.addActor(new VisLabel("Automaton"));

        HorizontalGroup buttonGroup = new HorizontalGroup();
        VerticalGroup addDelGroup = new VerticalGroup();

        ImageButton createButton = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "create"));
        Tooltip<Label> createTooltip = GuiUtils.getTooltip("Create new automaton");
        createButton.addListener(createTooltip);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog creationWindow = new CreateAutomatonWindow(gui, "Create new automaton");
                creationWindow.show(event.getStage());
            }
        });

        ImageButton deleteButton = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "delete"));
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
                            if (gui.getAutomaton() != null && result) gui.reload(null, null, null);
                        }
                );
                confirmDialog.show(event.getStage());
            }
        });

        ImageButton jython = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "python"));
        jython.addListener(GuiUtils.getTooltip("Load Python file"));
        jython.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser fc = new FileChooser("Select Python file", FileChooser.Mode.OPEN);
                fc.setMultiSelectionEnabled(false);
                fc.setDirectory(".");
                fc.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> files) {
                        PythonLoader loader = new PythonLoader(files.first());
                        loader.loadValues();
                        loader.loadRules();
                    }
                });
                event.getStage().addActor(fc);
            }
        });

        addDelGroup.addActor(createButton);
        addDelGroup.addActor(deleteButton);
        buttonGroup.addActor(addDelGroup);
        buttonGroup.addActor(jython);

        group.addActor(buttonGroup);
        group.pad(20);
    }

    public Actor getPane() {
        return group;
    }

}
