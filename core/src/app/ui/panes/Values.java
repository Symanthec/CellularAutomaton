package app.ui.panes;

public class Values {

    private final short[] values;
    private int at;

    public Values(short[] values) {
        this.values = values;
    }

    public void select(int i) {
        at = i % values.length;
    }

    public void next() {
        at = (at + 1) % values.length;
    }

    public short get() {
        return values[at];
    }

    public short[] all() {
        return values;
    }
}
