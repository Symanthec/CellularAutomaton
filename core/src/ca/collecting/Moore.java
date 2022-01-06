package ca.collecting;


import ca.world.World;

import java.lang.reflect.Array;

/***
 * Class for 2D dimensions
 * See: https://en.wikipedia.org/wiki/Moore_neighborhood
 */
public class Moore<C>  {

    private final int radius;
    private final Class<C> clazz;

    public Moore(Class<C> valueClass, int newRadius) {
        clazz = valueClass;
        radius = Math.abs(newRadius);
    }

    public Moore(Class<C> valueClass) {
        this(valueClass, 1);
    }

    public C[][] collect(World<C> world, int... position) {
        int x = position[0], y = position[1];
        C[][] array = (C[][]) Array.newInstance(clazz, width(), width());

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                array[i + radius][j + radius] = world.getCell(x + i, y + j);
            }
        }

        return array;
    }

    public int width() {
        return 2 * radius + 1;
    }

}
