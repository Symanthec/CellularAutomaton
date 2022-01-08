package ca.rules;

import ca.collecting.Moore;
import ca.values.Binary;
import ca.world.World;

import static ca.values.Binary.OFF;
import static ca.values.Binary.ON;

public class LifeRule implements Rule<Binary> {

    private final Moore<Binary> collector = new Moore<>(Binary.class);

    private static final Binary[] lut;
    private static final int aliveShift = 8;

    static {
        lut = new Binary[2 * 9]; // sum of alive cells in [0;8]
        for (int i = 0; i < lut.length; i++) {
            // sum of neighbor cells
            int sum = i % 9;

            // look in first 9 for dead cell, next 9 for living cell
            boolean alive = i > 9;

            // works based on "Game of Life" rules
            // See https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#:~:text=Any%20live%20cell,if%20by%20reproduction.
            if (alive) {
                lut[i] = (sum == 3 || sum == 2) ? ON : OFF;
            } else { // if dead
                lut[i] = (sum == 3) ? ON : OFF;
            }
        }
    }

    private final static int radius = 1;

    @Override
    public Class<Binary> supportedCells() {
        return Binary.class;
    }

    @Override
    public Binary produceValue(World<Binary> world, int... relativePosition) {
//        Binary[][] neighbors = collector.collect(world, relativePosition);
        int x = relativePosition[0], y = relativePosition[1];
        int lut_i = world.getCell(x, y).value ? aliveShift : 0;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                lut_i += world.getCell(x + i, y + j).value ? 1 : 0;
            }
        }

        return lut[lut_i];
    }

    @Override
    public String toString() {
        return "Game of Life";
    }
}
