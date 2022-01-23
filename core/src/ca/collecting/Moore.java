package ca.collecting;


import ca.world.World;

/***
 * Class for 2D dimensions
 * See: https://en.wikipedia.org/wiki/Moore_neighborhood
 */
public class Moore implements Collector {

    private final int radius;
    short[][] neighborhood;

    public Moore(int newRadius) {
        radius = Math.abs(newRadius);
    }

    public Moore() {
        this(1);
    }

    public void collect(World world, int... position) {
        int x = position[0], y = position[1];
        neighborhood = new short[width()][width()];

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                neighborhood[i + radius][j + radius] = world.getCell(x + i, y + j);
            }
        }
    }

    public int width() {
        return 2 * radius + 1;
    }

    @Override
    public int count(short value) {
        int c = 0;
        for (int i = 0; i < 2 * radius + 1; i++) {
            for (int j = 0; j < 2 * radius + 1; j++) {
                if (neighborhood[i][j] == value) {
                    c++;
                }
            }
        }
        return c;
    }
}
