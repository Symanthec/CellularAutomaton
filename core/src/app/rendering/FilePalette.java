package app.rendering;

import ca.values.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Random;

public class FilePalette implements Palette {

    HashMap<Value, Color> colorMap = new HashMap<>();

    public FilePalette(Value[] values, FileHandle file) {
        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(file);

        Random random = new Random();

        for (Value val : values) {
            JsonValue jcol = json.get(String.valueOf(val.hashCode()));

            Color color;
            try {
                color = new Color(
                        jcol.get(0).asFloat() / 255,
                        jcol.get(1).asFloat() / 255,
                        jcol.get(2).asFloat() / 255,
                        1f);
            } catch (NullPointerException e) {
                // color not found, create random color
                Gdx.app.error("COLOR", "Color for cell " + val + " not found. Choosing random");
                color = new Color(random.nextInt() | 0xFF); // random color + alpha 1.0
            }

            colorMap.put(val, color);
        }
    }

    @Override
    public Color getColorFor(Value val) {
        return colorMap.getOrDefault(val, Color.BLACK);
    }

}
