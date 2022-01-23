package ca.rules;

import ca.values.BrainCell;
import ca.world.World;

import static ca.values.BrainCell.*;

public class BrianBrain implements Rule {

    @Override
    public Class<BrainCell> supportedCells() {
        return BrainCell.class;
    }

    private final static int radius = 1;

    @Override
    public short produceValue(World world, int... relativePosition) {
        int x = relativePosition[0], y = relativePosition[1];
        short me = world.getCell(x,y);
        if (me == ALIVE) return DYING;
        if (me == DYING) return DEAD;
        else {
            int liveNeighbors = 0;
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    liveNeighbors += world.getCell(x + i, y + j) == ALIVE ? ALIVE : DEAD;
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
