package app.ui.panes;

import app.rendering.FilePalette;
import app.rendering.Palette;
import app.rendering.Renderer;
import app.ui.GUI;
import app.ui.GuiUtils;
import ca.Automaton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class EditingPane {

    private final VerticalGroup group;
    private Automaton automaton;
    private final VisTable paletteTable;
    private Values values;

    public EditingPane(GUI gui) {
        group = new VerticalGroup();

        VisLabel label = new VisLabel("Editing");
        paletteTable = new VisTable();

        HorizontalGroup buttons = new HorizontalGroup();
        ImageButton palette = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "palette"));
        palette.addListener(GuiUtils.getTooltip("Choose color palette"));
        palette.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser fc = new FileChooser("Select JSON color scheme", FileChooser.Mode.OPEN);
                fc.setMultiSelectionEnabled(false);
                fc.setDirectory(".");
                fc.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> files) {
                        FileHandle handle = files.first();
                        Values val = gui.getValues();
                        if (gui.getFrameManager() != null & val != null) {
                            FilePalette colPalette = new FilePalette(val.all(), handle);
                            gui.getFrameManager().setPalette(colPalette);
                            refreshColorButtons(colPalette);
                        } else {
                            Gdx.app.error("INIT","Create automaton first!");
                        }
                    }
                });
                event.getStage().addActor(fc);
            }
        });

        ImageButton clearButton = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "eraser"));
        clearButton.addListener(GuiUtils.getTooltip("Clear generation"));
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

        group.addActor(label);

        VerticalGroup funcButtons = new VerticalGroup();
        funcButtons.addActor(clearButton);
        funcButtons.addActor(palette);

        paletteTable.padRight(20);
        buttons.addActor(paletteTable);

        buttons.addActor(funcButtons);
        group.addActor(buttons);
        group.pad(20);
    }

    private void refreshColorButtons(Palette palette) {
        paletteTable.clearChildren();
        if (palette != null) {
            int i = 0;
            for (short value: values.all()) {
                Button button = createButton(palette.getColorFor(value));
                final int k = i++;
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        values.select(k);
                    }
                });
                button.pad(10);
                button.addListener(GuiUtils.getTooltip(String.valueOf(value)));
                paletteTable.add(button);
            }
        }
    }

    public void setAutomaton(Automaton automaton, Renderer renderer, Values values) {
        this.automaton = automaton;
        this.values = values;
        group.setTouchable(automaton == null ? Touchable.disabled: Touchable.enabled);
        refreshColorButtons(renderer != null ? renderer.getPalette(): null);
    }

    public Button createButton(Color col) {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(col);
        pixmap.fillRectangle(0,0,32,32);
        return new VisImageButton(
                new SpriteDrawable(new Sprite(new Texture(pixmap)))
        );
    }

    public Actor getPane() {
        return group;
    }

}
