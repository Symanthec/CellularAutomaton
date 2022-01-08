package ca.rules;

import ca.collecting.Moore;
import ca.values.WireCell;
import ca.world.World;

import static ca.values.WireCell.*;

public class WireworldRule implements Rule<WireCell>{

    private Moore<WireCell> collector = new Moore<>(WireCell.class);

    @Override
    public Class<WireCell> supportedCells() {
        return WireCell.class;
    }

    @Override
    public WireCell produceValue(World<WireCell> world, int... relativePosition) {
        WireCell me = world.getCell(relativePosition);
        if (me.equals(NONE)) return NONE;
        else if (me.equals(HEAD)) return TAIL;
        else if (me.equals(TAIL)) return WIRE;
        else {
            // WIRE case
            WireCell[][] neighbors = collector.collect(world, relativePosition);

            int heads = 0;
            for (int i = 0; i < collector.width(); i++) {
                for (int j = 0; j < collector.width(); j++) {
                    WireCell cell = neighbors[i][j];
                    if (!(i == 1 && j == 1) && cell.equals(HEAD)) heads++;
                    if (heads > 2) return WIRE;
                }
            }

            return (heads == 1 || heads == 2) ? HEAD: WIRE;
        }
    }

    @Override
    public String toString() {
        return "Wireworld";
    }
}
