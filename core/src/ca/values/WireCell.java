package ca.values;

public class WireCell extends DigitalValue{

    public static final WireCell
            NONE = new WireCell((byte) 0),
            WIRE = new WireCell((byte) 1),
            HEAD = new WireCell((byte) 2),
            TAIL = new WireCell((byte) 3);

    private static final WireCell[] vals = new WireCell[] { NONE, WIRE, HEAD, TAIL };

    public WireCell() {
        super(0L);
    }

    private WireCell(byte val) {
        super(val);
    }

    @Override
    public WireCell[] getValues() {
        return vals;
    }
}
