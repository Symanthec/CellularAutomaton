package ca.values;

public abstract class DigitalValue implements Value{

    public final long value;

    public DigitalValue() {
        this(0);
    }
    public DigitalValue(long newValue) {
        value = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DigitalValue brainCell = (DigitalValue) o;
        return value == brainCell.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
