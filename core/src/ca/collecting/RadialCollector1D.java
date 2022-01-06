package ca.collecting;

import ca.world.World;

import java.lang.reflect.Array;

public class RadialCollector1D<C> {

    public final int radius;
    private final Class<C> clazz;

    public RadialCollector1D(Class<C> cellClass) {
        this(cellClass, 1);
    }
    public RadialCollector1D(Class<C> cellClass, int newRadius) {
        clazz = cellClass;
        radius = Math.abs(newRadius);
    }

    public C[] collect(World<C> world, int[] current_position) {
        C[] array = (C[]) Array.newInstance(clazz, 2 * radius + 1);

        int x = current_position[0];

        for (int rel_x = -radius; rel_x <= radius; rel_x++) {
            array[rel_x] = world.getCell(x + rel_x);
        }

        return array;
    }

}
