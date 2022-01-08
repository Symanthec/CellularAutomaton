package app.rendering;

import ca.values.Value;
import ca.world.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public interface Renderer<V extends Value> extends Disposable {

    Texture render(World<V> world);

    void redrawCell(Texture texture, World<V> current, int[] pos);

    void setPalette(Palette palette);
}
