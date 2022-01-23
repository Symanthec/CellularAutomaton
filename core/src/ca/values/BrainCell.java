package ca.values;

public class BrainCell implements Cell {

    public static final short DEAD = 0, ALIVE = 1, DYING = 2;

    private static final short[] values = new short[] {0, 1, 2};

    @Override
    public short[] getValues() {
        return values;
    }

}
