package app.ui.panes;

import app.automaton.RuleRegistry;
import app.automaton.ValueRegistry;
import app.rendering.LatticeRenderer2D;
import app.rendering.Renderer;
import app.ui.GUI;
import app.ui.GuiUtils;
import ca.Automaton;
import ca.rules.Rule;
import ca.values.Cell;
import ca.values.ValueCollector;
import ca.world.World;
import ca.world.World1D;
import ca.world.World2D;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.*;

import java.util.Arrays;
import java.util.stream.Stream;

public class CreateAutomatonWindow extends Dialog {

    private final VisSelectBox<Class<? extends Cell>> valuesSelect = new VisSelectBox<Class<? extends Cell>>() {
        @Override
        protected GlyphLayout drawItem(Batch batch, BitmapFont font, Class<? extends Cell> item, float x, float y, float width) {
            String string = item.getSimpleName();
            return font.draw(batch, string, x, y, 0, string.length(), width, getAlign(), false, "...");
        }
    };
    private final VisSelectBox<Rule> rulesSelect = new VisSelectBox<>();

    private final String[] names = new String[]{
            "Width:",
            "Height:"
    };

    public CreateAutomatonWindow(GUI gui, String title) {
        super(title, VisUI.getSkin());
        getTitleLabel().setAlignment(Align.center);
        setResizable(false);

        // Select cell
        VerticalGroup layout = new VerticalGroup();

        valuesSelect.setItems(ValueRegistry.getAvailableValues());
        valuesSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Class<? extends Cell> clazz = valuesSelect.getSelected();
                Stream<? extends Rule> stream = Arrays.stream(RuleRegistry.getAvailableRules()).filter(rule -> rule.supportedCells().equals(clazz));
                rulesSelect.setItems(stream.toArray(Rule[]::new));
                pack();
            }
        });

        VerticalGroup dimsContainer = new VerticalGroup();
        dimsContainer.pad(20, 10, 15, 10);
        VisTable dims = new VisTable();
        VisTextButton addDim = new VisTextButton("New dimension");
        addDim.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VisTextField field = new VisTextField();
                field.setTextFieldFilter(new IntDigitsOnlyFilter(false));
                dims.add(names[dims.getRows()]).padBottom(10);
                dims.add(field).padLeft(10).row();
                pack();
                if (dims.getRows() >= 2)
                    dimsContainer.removeActor(addDim);
            }
        });
        dimsContainer.addActor(addDim);
        dimsContainer.addActor(dims);

        VisCheckBox checkBox = new VisCheckBox("Save all generations\n(big memory usage)");
        checkBox.padTop(10);
        checkBox.padBottom(15);

        HorizontalGroup yesNoGroup = new HorizontalGroup();
        ImageButton yes = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "ok"));
        yes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                short[] values = ValueCollector.collectValues(valuesSelect.getSelected());
                if (values == null) {
                    Dialogs.showErrorDialog(event.getStage(), "Select cell type");
                    return;
                }
                short defaultCell = values[0];

                Rule rule = rulesSelect.getSelected();
                if (rule == null) {
                    Dialogs.showErrorDialog(event.getStage(), "Select rule type");
                    return;
                }

                int dimensions = dims.getChildren().size / 2;
                int[] dimSizes = new int[dimensions];
                for (int i = 0; i < dimensions; i++) {
                    VisTextField field = (VisTextField) dims.getChildren().get(1 + 2 * i);
                    try {
                        dimSizes[i] = Integer.parseInt(field.getText());
                        if (dimSizes[i] <= 0) return;
                    } catch (NumberFormatException e) {
                        Dialogs.showErrorDialog(event.getStage(), "Invalid number: " + field.getText());
                        return;
                    }
                }

                World seed;
                switch (dimensions) {
                    case 1:
                        seed = new World1D(defaultCell, dimSizes[0]);
                        break;
                    case 2:
                        seed = new World2D(defaultCell, dimSizes[0], dimSizes[1]);
                        break;
                    default:
                        return;
                }

                Automaton automaton = new Automaton(rulesSelect.getSelected());
                automaton.setRoot(seed);

                Renderer renderer = new LatticeRenderer2D(valuesSelect.getSelected());
                automaton.setSaveAll(checkBox.isChecked());

                gui.reload(automaton, renderer, new Values(values));
                hide();
            }
        });

        ImageButton no = new ImageButton(GuiUtils.getSprite(gui.getAtlas(), "delete"));
        no.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        yes.pad(0, 10, 0, 10);
        no.pad(0, 10, 0, 10);

        yesNoGroup.addActor(yes);
        yesNoGroup.addActor(no);

        layout.addActor(valuesSelect);
        layout.addActor(dimsContainer);
        layout.addActor(rulesSelect);
        layout.addActor(checkBox);
        layout.addActor(yesNoGroup);
        add(layout);
        pack();
    }

}
