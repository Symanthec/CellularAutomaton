package app.ui.panes;

import app.Application;
import app.ui.GuiUtils;
import app.ui.GUI;
import ca.Automaton;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;

public class EvolvingPane {

    private final VisWindow window;
    private final Automaton automaton;

    public EvolvingPane(Automaton automaton) {
        this.automaton = automaton;
        this.window = new VisWindow("Evolving", false);
        window.setResizable(false);
        window.setMovable(false);
        TextureAtlas atlas = GUI.atlas;

        VisLabel gen_label = new VisLabel("Generation: 0");
        ////////////////////////
        ///// SLIDER GROUP /////
        ////////////////////////
        Button play_pause = new Button(GuiUtils.getSprite(atlas, "play"), null, GuiUtils.getSprite(atlas, "pause"));
        play_pause.addListener(GuiUtils.getTooltip("Play/Pause animation"));
        play_pause.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Application.setEvolveCount(Application.getEvolveCount() != 0 ? 0 : -1L);
                return true;
            }
        });
        play_pause.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                ((Button) getActor()).setChecked(Application.getEvolveCount() != 0);
                return true;
            }
        });

        Button left = new Button(GuiUtils.getSprite(atlas, "left_arrow"));
        left.addListener(GuiUtils.getTooltip("Previous generation"));
        left.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int pos = Math.max(automaton.at() - 1, 0);
                automaton.select(pos);
                gen_label.setText("Generation: " + pos);
                return true;
            }
        });

        VisSlider slider = new VisSlider(0, automaton.size(), 1, false);
        slider.addListener(GuiUtils.getTooltip("Select generation"));
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                automaton.select((int) slider.getValue());
                gen_label.setText("Generation: " + automaton.at());
            }
        });
        slider.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                slider.setRange(0, automaton.size()-1);
                slider.setValue(automaton.at());
                return false;
            }
        });

        Button right = new Button(GuiUtils.getSprite(atlas, "right_arrow"));
        right.addListener(GuiUtils.getTooltip("Next generation"));
        right.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (automaton.at() == automaton.size() - 1) {
                    automaton.evolve();
                } else automaton.select(automaton.at() + 1);

                gen_label.setText("Generation: " + automaton.at());
                return true;
            }
        });

        HorizontalGroup gen_slider = new HorizontalGroup();
        gen_slider.addActor(play_pause);
        gen_slider.addActor(left);
        gen_slider.addActor(slider);
        gen_slider.addActor(right);


        VisTextField evolveForArea = new VisTextField("0");

        Button play_until = new Button(GuiUtils.getSprite(atlas, "until_end"));
        play_until.addListener(GuiUtils.getTooltip("Play until (negative = infinite)"));
        play_until.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    String input = evolveForArea.getText();
                    long gens = Long.parseLong(input);
                    Application.setEvolveCount(gens);
                } catch (NumberFormatException e) {
                    Application.setEvolveCount(0);
                    VisDialog dialog = new VisDialog("Invalid number.");
                    dialog.button("OK");
                    dialog.show(event.getStage());
                }
                return true;
            }
        });

        HorizontalGroup animation_group = new HorizontalGroup();
        animation_group.addActor(new VisLabel("Evolve for: "));
        animation_group.addActor(evolveForArea);
        animation_group.addActor(play_until);

        VerticalGroup vert = new VerticalGroup();
        vert.addActor(gen_label);
        vert.addActor(gen_slider);
        vert.addActor(animation_group);

        window.add(vert);
    }

    public Actor getPane() {
        return window;
    }

}
