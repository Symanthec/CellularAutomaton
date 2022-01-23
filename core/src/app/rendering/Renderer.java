package app.rendering;

import ca.world.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public interface Renderer extends Disposable {

    Texture render(World world);

    void redrawCell(Texture texture, World current, int[] pos);

    void setPalette(Palette palette);

    Palette getPalette();
}
