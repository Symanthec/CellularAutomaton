package app.ui.panes;

import ca.values.Value;

public class Values {

    private final Value[] values;
    private int at;

    public Values(Value[] values) {
        this.values = values;
    }

    public void select(int i) {
        at = i % values.length;
    }

    public void next() {
        at = (at + 1) % values.length;
    }

    public Value get() {
        return values[at];
    }

    public Value[] all() {
        return values;
    }
}
