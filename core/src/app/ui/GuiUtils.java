package app.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.VisUI;

public class GuiUtils {

    public static SpriteDrawable getSprite(TextureAtlas atlas, String name) {
        return new SpriteDrawable(atlas.createSprite(name));
    }

    public static TextTooltip getTooltip(String text) {
        TextTooltip tooltip = new TextTooltip(text, VisUI.getSkin());
        tooltip.setInstant(true);
        return tooltip;
    }

}
