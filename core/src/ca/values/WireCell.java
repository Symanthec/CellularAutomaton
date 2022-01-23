package ca.values;

public class WireCell implements Cell {

    public static final short
            NONE = 0,
            WIRE = 1,
            HEAD = 2,
            TAIL = 3;

    private static final short[] values = new short[] { NONE, WIRE, HEAD, TAIL };

    @Override
    public short[] getValues() {
        return values;
    }

}
