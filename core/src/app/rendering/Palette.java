package app.rendering;

import ca.values.Value;
import com.badlogic.gdx.graphics.Color;

public interface Palette {

    Color getColorFor(Value val);

}
