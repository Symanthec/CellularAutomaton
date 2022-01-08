package app.rendering;

import ca.values.Value;
import ca.values.ValueCollector;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class RandomPalette<V extends Value> implements Palette {

    private final V[] values;
    private final Color[] colors;
    private final Color defaultColor;

    public RandomPalette(Class<V> clazz, Color defaultColor) {
        V[] values = (V[]) ValueCollector.collectValues(clazz);
        if (values == null) throw new NullPointerException("ValueCollector didn't gather value list from given class:" + clazz.getCanonicalName());

        Color[] colorArray = new Color[values.length];
        Random rand = new Random();
        for (int i = 0; i < colorArray.length; i++) {
            colorArray[i] = new Color(rand.nextInt() | 255);
        }

        this.defaultColor = defaultColor;
        this.values = values;
        this.colors = colorArray;
    }

    public Color getColorFor(Value cellValue) {
        if (cellValue == null) throw new NullPointerException("cellValue cannot be null");
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(cellValue))
                return colors[i];
        }
        return defaultColor;
    }

}
