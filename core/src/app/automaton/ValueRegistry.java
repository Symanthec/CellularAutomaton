package app.automaton;

import ca.values.Binary;
import ca.values.BrainCell;
import ca.values.Cell;
import ca.values.WireCell;

import java.util.Vector;

public class ValueRegistry {

    private static final Vector<Class<? extends Cell>> values = new Vector<>();

    static {
        values.add(Binary.class);
        values.add(WireCell.class);
        values.add(BrainCell.class);
    }

    public static void registerValue(Class<? extends Cell> clazz) {
        if (!values.contains(clazz))
            values.add(clazz);
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Cell>[] getAvailableValues() {
        return values.toArray(new Class[0]);
    }

}
