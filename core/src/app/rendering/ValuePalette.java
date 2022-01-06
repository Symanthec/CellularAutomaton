package app.rendering;

import ca.values.Value;
import ca.values.ValueCollector;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class ValuePalette<V extends Value> {

    private final V[] values;
    private final Color[] colors;
    private final Color defaultColor = Color.WHITE;

    public ValuePalette(V[] valueArray, Color[] colorMap) {
        values = valueArray;
        colors = colorMap;
    }

    public ValuePalette(Class<V> clazz, Color defaultColor) {
        V[] values = (V[]) ValueCollector.collectValues(clazz);
        if (values == null) throw new NullPointerException("ValueCollector didn't gather value list from given class:" + clazz.getCanonicalName());

        Color[] colorArray = new Color[values.length];
        Random rand = new Random();
        for (int i = 0; i < colorArray.length; i++) {
            colorArray[i] = new Color(rand.nextInt());
        }

        this.values = values;
        this.colors = colorArray;
    }

    public V[] getValues() {
        return values;
    }

    public Color getColorFor(V cellValue) {
        if (cellValue == null) throw new NullPointerException("cellValue cannot be null");
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(cellValue))
                return colors[i];
        }
        return defaultColor;
    }

}
