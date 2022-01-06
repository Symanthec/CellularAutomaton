package ca.world;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

public class World1D<C> implements World<C> {

    protected C[] array;
    public final int[] size;
    public final C defaultValue;

    public World1D(C fillValue, int size) {
        this.size = new int[]{size};
        this.defaultValue = fillValue;

        array = (C[]) Array.newInstance(fillValue.getClass(), size);
        Arrays.fill(array, fillValue);
    }

    @Override
    public C getCell(int[] coordinates) {
        try{
            return array[coordinates[0]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    @Override
    public void setCell(C newValue, int... coordinates) {
        array[coordinates[0]] = newValue;
    }

    public void setCell(C newValue, int x) {
        array[x] = newValue;
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
                (x) -> new int[]{x}
        ).iterator();
    }

    @Override
    public World<C> copy() {
        return new World1D<>(defaultValue, size[0]);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (C val: array)
            result.append(val.toString());
        return result.toString();
    }
}

