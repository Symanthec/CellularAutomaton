package app.rendering;

import ca.values.Cell;
import ca.values.ValueCollector;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Random;

public class RandomPalette implements Palette {

    private final Color defaultColor;

    HashMap<Short, Color> map;

    public RandomPalette(Class<? extends Cell> clazz, Color defaultColor) {
        short[] values = ValueCollector.collectValues(clazz);
        if (values == null) throw new NullPointerException("ValueCollector didn't gather value list from given class:" + clazz.getCanonicalName());

        map = new HashMap<>(values.length);
        Random rand = new Random();
        for (short val: values) {
            map.put(val, new Color(rand.nextInt() | 255));
        }

        this.defaultColor = defaultColor;
    }

    public Color getColorFor(short cellValue) {
        return map.getOrDefault(cellValue, defaultColor);
    }

}
