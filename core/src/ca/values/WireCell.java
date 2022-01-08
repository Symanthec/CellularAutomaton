package ca.values;

public class WireCell extends DigitalValue{

    public static final WireCell
            NONE = new WireCell((byte) 0),
            WIRE = new WireCell((byte) 1),
            HEAD = new WireCell((byte) 2),
            TAIL = new WireCell((byte) 3);

    private static final WireCell[] values = new WireCell[] { NONE, WIRE, HEAD, TAIL };

    public WireCell() {
        super(0);
    }

    private WireCell(byte val) {
        super(val);
    }

    @Override
    public WireCell[] getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        switch(value) {
            case 0:
                return "NONE";
            case 1:
                return "WIRE";
            case 2:
                return "HEAD";
            case 3:
                return "TAIL";
            default:
                return "unknown";
        }
    }
}
