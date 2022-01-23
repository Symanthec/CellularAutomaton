package ca.values;

public class Binary implements Cell {

    public static final short ON = 1, OFF = 0;
    private static final short[] values = new short[] {OFF, ON};

    @Override
    public short[] getValues() {
        return values;
    }

}
