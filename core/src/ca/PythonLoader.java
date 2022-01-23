package ca;

import app.automaton.RuleRegistry;
import app.automaton.ValueRegistry;
import ca.rules.Rule;
import ca.values.Cell;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.python.core.PyList;
import org.python.core.PyListIterator;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.lang.reflect.InvocationTargetException;

public class PythonLoader {

    private final PyObject classesList;

    private boolean rulesLoaded = false, cellsLoaded = false;

    public PythonLoader(FileHandle filename) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(filename.path());

        classesList = interpreter.get("get_classes").__call__();
    }

    public void loadRules() {
        if (!rulesLoaded) {
            PyListIterator iterator = new PyListIterator((PyList) classesList.__getitem__(new PyString("rules")));
            iterator.forEach( t -> {
                try {
                    @SuppressWarnings("unchecked")
                    Rule rule = ((Class<? extends Rule>) t).getConstructor().newInstance();
                    RuleRegistry.registerRule(rule);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    Gdx.app.error("PYTHON LOAD", e.getMessage(), e);
                }
            });

            rulesLoaded = true;
        }
    }

    public void loadValues() {
        if (!cellsLoaded) {
            PyListIterator iterator = new PyListIterator((PyList) classesList.__getitem__(new PyString("cells")));
            iterator.forEach( t -> {
                try{
                    //noinspection unchecked
                    ValueRegistry.registerValue((Class<? extends Cell>) t);
                } catch (Exception e) {
                    Gdx.app.error("PYTHON LOAD", e.getMessage(), e);
                }
            });

            cellsLoaded = true;
        }
    }

}
