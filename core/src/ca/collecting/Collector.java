package ca.collecting;

import ca.world.World;

public interface Collector {

    void collect(World world, int[] pos);

    int count(short value);

}
