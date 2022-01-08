package ca.rules;

import ca.values.BrainCell;
import ca.world.World;

import static ca.values.BrainCell.*;

public class BrianBrain implements Rule<BrainCell> {

    @Override
    public Class<BrainCell> supportedCells() {
        return BrainCell.class;
    }

    private final static int radius = 1;

    @Override
    public BrainCell produceValue(World<BrainCell> world, int... relativePosition) {
        int x = relativePosition[0], y = relativePosition[1];
        BrainCell me = world.getCell(x,y);
        if (me.equals(ALIVE)) return DYING;
        if (me.equals(DYING)) return DEAD;
        else {
            int liveNeighbors = 0;
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    liveNeighbors += world.getCell(x + i, y + j).value == 1 ? 1 : 0;
                }
            }

            return liveNeighbors == 2 ? ALIVE : DEAD;
        }
    }

    @Override
    public String toString() {
        return "BrianBrain";
    }
}
