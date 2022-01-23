package ca.world;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.IntStream;

public class World1D implements World {

    protected short[] array;
    public final int[] size;
    public final short defaultValue;

    public World1D(short fillValue, int size) {
        this.size = new int[]{size, 1, 1};
        this.defaultValue = fillValue;

        array = new short[size];
        Arrays.fill(array, fillValue);
    }

    @Override
    public short getCell(int[] coordinates) {
        try{
            return array[coordinates[0]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    @Override
    public void setCell(short newValue, int... coordinates) {
        array[coordinates[0]] = newValue;
    }

    @Override
    public void reset() {
        Arrays.fill(array, defaultValue);
    }

    @Override
    public int[] getBounds() {
        return size;
    }

    @Override
    public Iterator<int[]> iterator() {
        return IntStream.range(0, size[0]).mapToObj(
                (x) -> new int[]{x, 0, 0}
        ).iterator();
    }

    @Override
    public World copy() {
        return new World1D(defaultValue, size[0]);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        NumberFormat format = new DecimalFormat(String.join("", Collections.nCopies(5, "0")));
        for (short val: array)
            result.append(' ').append(format.format(val)).append(' ');
        return result.toString();
    }
}

