package ca.values;

public class BrainCell implements Value {

    public final byte value;
    public static final BrainCell DEAD = new BrainCell((byte) 0), ALIVE = new BrainCell((byte) 1), DYING = new BrainCell((byte) 2);

    private final BrainCell[] values = new BrainCell[] {DEAD, ALIVE, DYING};

    public BrainCell() {
        this(DEAD.value);
    }

    public BrainCell(byte value) {
        this.value = value;
    }

    @Override
    public BrainCell[] getValues() {
        return values;
    }

}
