package ca;

import ca.rules.Rule;
import ca.values.Value;
import org.python.core.PyList;
import org.python.core.PyListIterator;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

public class PythonLoader {

    private PythonInterpreter interpreter;
    private String filename;

    private final String RULES_KEY = "rules", CELLS_KEY = "values";
    private PyObject typesDict;

    private Rule[] rules = null;
    private Value[] cells = null;

    public PythonLoader(String filename) {
        interpreter = new PythonInterpreter();
        interpreter.execfile(filename);

        typesDict = interpreter.get("get_classes").__call__();
    }

    public Rule[] getRules() {
        if (rules == null) {
            Vector<Rule> rulesVec = new Vector<>();

            PyList list = (PyList) typesDict.__getitem__(new PyString(RULES_KEY));
            PyListIterator iterator = new PyListIterator(list);
            iterator.forEach( t -> {
                try {
                    Rule rule = ((Class<Rule>) t).getConstructor().newInstance();
                    rulesVec.add(rule);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            rules = new Rule[rulesVec.size()];
            rulesVec.toArray(rules);
        }

        return rules;
    }

    public Value[] getValues() {
        if (cells == null) {
            Vector<Value> cellsVec = new Vector<>();
            PyList list = (PyList) typesDict.__getitem__(new PyString(CELLS_KEY));
            PyListIterator iterator = new PyListIterator(list);
            iterator.forEach( t -> {
                try {
                    Value rule = ((Class<Value>) t).getConstructor().newInstance();
                    cellsVec.add(rule);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            cells = new Value[cellsVec.size()];
            cellsVec.toArray(cells);
        }

        return cells;
    }
}
