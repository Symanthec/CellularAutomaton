package ca.rules;

import ca.world.World;

public interface Rule {

    Class<?> supportedCells();

    short produceValue(World world, int... relativePosition);

}
