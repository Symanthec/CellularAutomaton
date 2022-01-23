package ca.world;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class World3D implements World, Iterable<int[]>{

    public final int WIDTH = 0, HEIGHT = 1, DEPTH = 2;

    protected short[][][] world;
    private final short defaultValue;
    public final int[] bounds;

    public World3D(short fillValue, int... bounds) {
        this.bounds = new int[] {
            bounds[0],
            bounds[1],
            bounds[2]
        };

        this.defaultValue = fillValue;

        world = (short[][][]) Array.newInstance(Short.TYPE, bounds);
    }

    @Override
    public short getCell(int... coordinates) {
        try{
            return world
                    [coordinates[0]] // x position
                    [coordinates[1]] // y position
                    [coordinates[2]];// z position
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    @Override
    public void setCell(short newValue, int... coordinates) {
        world[coordinates[0]][coordinates[1]][coordinates[2]] = newValue;
    }

    @Override
    public void reset() {
        world = (short[][][]) Array.newInstance(Short.TYPE, bounds);
    }

    @Override
    public int[] getBounds() {
        return bounds;
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator3D(new int[]{
                0, bounds[WIDTH],
                0, bounds[HEIGHT],
                0, bounds[DEPTH]
        });
    }

    @Override
    public World copy() {
        return null;
    }

    private static class Iterator3D implements Iterator<int[]> {

        private int at_x, at_y, at_z;
        private final int
                start_x, end_x,
                start_y, end_y,
                end_z;

        Iterator3D(int [] bounds) {
            start_x = Math.min(bounds[0], bounds[1]);
            start_y = Math.min(bounds[2], bounds[3]);
            // start_x omitted as iterated last and once

            end_x = Math.max(bounds[0], bounds[1]);
            end_y = Math.max(bounds[2], bounds[3]);
            end_z = Math.max(bounds[4], bounds[5]);

            at_x = start_x - 1;
            at_y = start_y;
            at_z = Math.min(bounds[4], bounds[5]);
        }

        @Override
        public boolean hasNext() {
            return (at_z < end_z - 1) || (at_y < end_y - 1) || (at_x < end_x - 1);
        }

        @Override
        public int[] next() {
            if (++at_x >= end_x) {
                // reached the end of 0x
                // go to the start
                at_x = start_x;
                at_y++; // shift on 0y
            }

            if (at_y >= end_y) {
                at_y = start_y;
                at_z++;
            }

            if (at_z >= end_z)
                throw new NoSuchElementException("Run out of cells");
            return new int[]{at_x, at_y, at_z};
        }
    }

}
