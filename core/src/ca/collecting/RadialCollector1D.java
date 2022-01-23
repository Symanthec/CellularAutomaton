package ca.collecting;

import ca.world.World;

public class RadialCollector1D implements Collector{

    public final int radius;
    private short[] neighborhood;

    public RadialCollector1D() {
        this(1);
    }

    public RadialCollector1D(int newRadius) {
        radius = Math.abs(newRadius);
    }

    public void collect(World world, int[] current_position) {
        neighborhood = new short[2 * radius + 1];

        int x = current_position[0];

        for (int rel_x = -radius; rel_x <= radius; rel_x++) {
            neighborhood[radius + rel_x] = world.getCell(x + rel_x);
        }
    }

    @Override
    public int count(short value) {
        int c = 0;
        for (short s: neighborhood) {
            if (s == value)
                c++;
        }
        return c;
    }

}
