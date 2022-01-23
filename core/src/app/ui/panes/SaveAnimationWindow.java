package app.ui.panes;

import app.rendering.FrameManager;
import app.ui.GUI;
import ca.Automaton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;

public class SaveAnimationWindow extends VisDialog {

    // digits in file name, identifying frame
    private final int digits = (int) Math.ceil(Math.log10(Integer.MAX_VALUE));

    private FileHandle handle;

    public SaveAnimationWindow(GUI gui) {
        this(gui, "Save as .mp4 file");
    }

    public SaveAnimationWindow(GUI gui, String title) {
        super(title);
        addCloseButton();

        VerticalGroup group = new VerticalGroup();
        group.addActor(new VisLabel("Please, select frame range [from:to]"));

        VisTextField fromField = new VisTextField();
        VisTextField toField = new VisTextField();
        VisTextField framerateField = new VisTextField();

        fromField.setTextFieldFilter(new IntDigitsOnlyFilter(false));
        toField.setTextFieldFilter(new IntDigitsOnlyFilter(false));
        framerateField.setTextFieldFilter(new IntDigitsOnlyFilter(false));

        VisLabel path = new VisLabel();
        VisTextButton selectFile = new VisTextButton("Select file");
        selectFile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser fc = new FileChooser(FileChooser.Mode.SAVE);
                fc.setMultiSelectionEnabled(false);
                fc.setDirectory(".");
                fc.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> files) {
                        handle = files.first();
                        path.setText(handle.path());
                        pack();
                    }
                });
                event.getStage().addActor(fc);
            }
        });

        VisTable formTable = new VisTable();
        formTable.padTop(10);
        formTable.add(new VisLabel("From: ")).left();
        formTable.add(fromField).padBottom(10).row();
        formTable.add(new VisLabel("To: ")).left();
        formTable.add(toField).padBottom(10).row();
        formTable.add(new VisLabel("Framerate: ")).left();
        formTable.add(framerateField).padBottom(10).row();
        formTable.add(path).left();
        formTable.add(selectFile);
        group.addActor(formTable);

        VisTextButton start = new VisTextButton("Export");
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Automaton auto = gui.getAutomaton();
                FrameManager frames = gui.getFrameManager();
                if (auto != null && frames != null) {
                    // init
                    int from = Integer.parseInt(fromField.getText());
                    int to = Integer.parseInt(toField.getText());
                    int framerate = Integer.parseInt(framerateField.getText());

                    if (from < 0) {
                        Gdx.app.error("ERROR","Invalid from field.");
                        return;
                    } else if (to >= auto.size()) {
                        Gdx.app.error("ERROR","To exceeds automaton size");
                        return;
                    } else if (from >= to) {
                        Gdx.app.error("ERROR","'From' frame exceeds 'to' frame");
                        return;
                    } else if (framerate <= 0) {
                        Gdx.app.error("ERROR", "Invalid framerate");
                    }

                    if (handle.exists() || handle.isDirectory()) {
                        Gdx.app.error("FILE", "File either exists or is a directory. Select another path");
                    }

                    TextureExporter exporter = new TextureExporter();
                    NumberFormat format = new DecimalFormat(String.join("", Collections.nCopies(digits, "0")));
                    FileHandle cacheFolder = new FileHandle(handle.parent().path() + "/cache");
                    cacheFolder.mkdirs();
                    for (int i = 0; i <= to - from; i++) {
                        FileHandle file = new FileHandle(cacheFolder + "/" + format.format(i) + ".png");
                        exporter.saveImage(frames.getFrameOrRender(i + from, auto.get(i+from)), file);
                    }
                    exporter.dispose();

                    new Thread(() -> {
                        Runtime rt = Runtime.getRuntime();
                        FileHandle ffmpeg = new FileHandle("ffmpeg/ffmpeg.exe");
                        try {
                            String[] command = new String[] {
                                    ffmpeg.path(),
                                    "-r",
                                    String.valueOf(framerate),
                                    "-i",
                                    cacheFolder.path() + "/%" + digits + "d.png",
                                    handle.path()
                            };
                            rt.exec(String.join(" ", command));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }).start();
                }


            }
        });
        group.addActor(start);

        add(group);
        pack();
    }

}
