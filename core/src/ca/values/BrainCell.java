package ca.values;

public class BrainCell implements Value {

    public final byte value;
    public static final BrainCell DEAD = new BrainCell((byte) 0), ALIVE = new BrainCell((byte) 1), DYING = new BrainCell((byte) 2);

    private static final BrainCell[] values = new BrainCell[] {DEAD, ALIVE, DYING};

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrainCell brainCell = (BrainCell) o;

        return value == brainCell.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        switch (value) {
            case 0:
                return "DEAD";
            case 1:
                return "ALIVE";
            case 2:
                return "DYING";
            default:
                return "unknown";
        }
    }
}
