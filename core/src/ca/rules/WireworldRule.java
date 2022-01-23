package ca.rules;

import ca.collecting.Moore;
import ca.values.WireCell;
import ca.world.World;

import static ca.values.WireCell.*;

public class WireworldRule implements Rule {

    private Moore collector = new Moore();

    @Override
    public Class<WireCell> supportedCells() {
        return WireCell.class;
    }

    @Override
    public short produceValue(World world, int... relativePosition) {
        short me = world.getCell(relativePosition);
        if (me == NONE) return NONE;
        else if (me == HEAD) return TAIL;
        else if (me == TAIL) return WIRE;
        else {
            // WIRE case
            collector.collect(world, relativePosition);
            int heads = collector.count(HEAD);
            return (heads == 1 || heads == 2) ? HEAD: WIRE;
        }
    }

    @Override
    public String toString() {
        return "Wireworld";
    }
}
