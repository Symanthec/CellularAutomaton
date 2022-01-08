package app.ui.panes;

import app.automaton.EvolutionThread;
import app.ui.GUI;
import app.ui.GuiUtils;
import ca.Automaton;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;

public class EvolvingPane {

    private final VerticalGroup pane;
    private Automaton<?> automaton;

    public EvolvingPane(GUI gui) {
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
                EvolutionThread evo = gui.getEvolver();
                evo.setEvolveCount(evo.getEvolveCount() != 0 ? 0 : -1L);
                return true;
            }
        });
        play_pause.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                EvolutionThread evo = gui.getEvolver();
                ((Button) getActor()).setChecked(evo.getEvolveCount() != 0);
                return true;
            }
        });

        Button left = new Button(GuiUtils.getSprite(atlas, "left_arrow"));
        left.addListener(GuiUtils.getTooltip("Previous generation"));
        left.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (automaton != null) {
                    int pos = Math.max(automaton.at() - 1, 0);
                    automaton.select(pos);
                    gen_label.setText("Generation: " + pos);
                }
                return true;
            }
        });


        VisSlider slider = new VisSlider(0, 1, 1, false);
        slider.addListener(GuiUtils.getTooltip("Select generation"));
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (automaton != null) {
                    automaton.select((int) slider.getValue());
                    gen_label.setText("Generation: " + automaton.at());
                }
            }
        });
        slider.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (automaton != null) {
                    slider.setRange(0, automaton.size()-1);
                    slider.setValue(automaton.at());
                }
                return false;
            }
        });

        Button right = new Button(GuiUtils.getSprite(atlas, "right_arrow"));
        right.addListener(GuiUtils.getTooltip("Next generation"));
        right.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (automaton != null) {
                    if (automaton.at() == automaton.size() - 1) {
                        automaton.evolve();
                    } else automaton.select(automaton.at() + 1);

                    gen_label.setText("Generation: " + automaton.at());
                }
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
                    gui.getEvolver().setEvolveCount(gens);
                } catch (NumberFormatException e) {
                    gui.getEvolver().setEvolveCount(0);
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

        pane = new VerticalGroup();
        pane.padRight(50);
        pane.addActor(new VisLabel("Evolution"));
        pane.addActor(gen_slider);
        pane.addActor(animation_group);
        pane.addActor(gen_label);
    }

    public Actor getPane() {
        return pane;
    }

    public void setAutomaton(Automaton<?> automaton) {
        this.automaton = automaton;
        pane.setTouchable( automaton != null ? Touchable.enabled: Touchable.disabled);
    }
}
