package ca.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class World2D implements World, Iterable<int[]> {

    public static final int WIDTH = 0, HEIGHT = 1;

    protected short[][] lattice;
    short defaultValue;
    protected final int[] bounds;
    public final int width, height;

    public World2D(short default_value, int start_width, int start_height) {
        this.bounds = new int[] {start_width, start_height, 1};
        this.width = start_width;
        this.height = start_height;
        this.defaultValue = default_value;

        this.lattice = new short[start_width][start_height];

        for (short[] row: lattice)
            Arrays.fill(row, defaultValue);
    }

    @Override
    public short getCell(int... coordinates) {
        int x = coordinates[0], y = coordinates[1];
        if (0 <= x && 0 <= y && x < width && y < height)
            return lattice[x][y];
        else return defaultValue;
    }

    @Override
    public void setCell(short new_value, int... coordinates) {
        lattice[coordinates[0]][coordinates[1]] = new_value;
    }

    @Override
    public void reset() {
        for (short[] row: lattice)
            Arrays.fill(row, defaultValue);
    }

    @Override
    public int[] getBounds() {
        return bounds;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int column = 0; column < bounds[HEIGHT]; column++) {
            for (int row = 0; row < bounds[WIDTH]; row++) {
                short cell = getCell(row, column);
                result.append(cell);
            }
            result.append('\n');
        }
        return result.toString();
    }

    public World2D copy() {
        return new World2D(defaultValue, bounds[WIDTH], bounds[HEIGHT]);
    }

    @Override
    public Iterator<int[]> iterator() {
        ArrayList<int[]> coordinates = new ArrayList<>();
        for (int column = 0; column < bounds[HEIGHT]; column++) {
            for (int row = 0; row < bounds[WIDTH]; row++) {
                coordinates.add(new int[]{row, column, 0});
            }
        }

        return coordinates.iterator();
    }
}
