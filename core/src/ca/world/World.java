package ca.world;

import java.util.Iterator;

public interface World<C> extends Iterable<int[]> {

    /*
     * Getters and setters for cell array
     */
    C getCell(int... coordinates);
    void setCell(C newValue, int... coordinates) throws IndexOutOfBoundsException;

    void reset();

    int[] getBounds();

    /*
     * Returns iterator of coordinates of each cell
     * This is needed to let Neighborhood collect neighbors.
     */
    Iterator<int[]> iterator();
    World<C> copy();

}
