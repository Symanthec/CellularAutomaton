package ca.values;

public class Binary implements Value{

    public static final Binary ON = new Binary(true), OFF = new Binary(false);
    private static final Binary[] vals = new Binary[] {OFF, ON};

    public final boolean value;

    // protected constructor used by ValuesCollector
    public Binary() {
        this(OFF.value);
    }

    public Binary(boolean isOn) {
        value = isOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Binary binary = (Binary) o;

        return value == binary.value;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public Binary[] getValues() {
        return vals;
    }
}
