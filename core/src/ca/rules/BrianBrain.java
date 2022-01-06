package ca.rules;

import ca.values.BrainCell;
import ca.world.World;

public class BrianBrain implements Rule<BrainCell> {

    @Override
    public Class<BrainCell> supportedCells() {
        return BrainCell.class;
    }

    @Override
    public BrainCell produceValue(World<BrainCell> world, int... relativePosition) {
        return null;
    }
}
