package ca.rules;

import ca.world.World;

public interface Rule<C> {

    Class<C> supportedCells();

    C produceValue(World<C> world, int... relativePosition);

}
