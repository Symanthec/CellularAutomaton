package app.ui.panes;

import app.ui.GUI;
import app.ui.GuiUtils;
import ca.Automaton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class ExportPane {

    private final VerticalGroup group;
    private final TextureExporter exporter = new TextureExporter();

    public ExportPane(GUI gui) {
        group = new VerticalGroup();

        ImageButton exportPic = new ImageButton(GuiUtils.getSprite(GUI.atlas, "camera"));
        exportPic.addListener(GuiUtils.getTooltip("Export as .png image"));
        exportPic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Automaton<?> auto = gui.getAutomaton();
                if (auto == null) return;
                int index = auto.at(); // remember current frame
                FileChooser fc = new FileChooser(FileChooser.Mode.SAVE);
                fc.setMultiSelectionEnabled(false);
                fc.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> files) {
                        Texture tex = gui.getFrameManager().getFrameOrRender(index, auto.get(index));
                        exporter.saveImage(tex, files.first());
                    }
                });
                event.getStage().addActor(fc);
            }
        });

        ImageButton exportVid = new ImageButton(GuiUtils.getSprite(GUI.atlas, "video"));
        exportVid.addListener(GuiUtils.getTooltip("Export as .mp4 animation"));
        exportVid.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gui.getAutomaton() == null) {
                    Gdx.app.error("INIT", "Create automaton first");
                    return;
                }

                FileHandle ffmpeg = new FileHandle("ffmpeg/ffmpeg.exe");
                if (!ffmpeg.exists()) {
                    Gdx.app.error("FFMPEG", "ffmpeg.exe not found at 'ffmpeg' folder.");
                    return;
                }

                event.getStage().addActor(new SaveAnimationWindow(gui));
            }
        });

        group.addActor(new VisLabel("Export"));
        group.addActor(exportVid);
        group.addActor(exportPic);
        group.padRight(20);
    }

    public Actor getPane() {
        return group;
    }

}
