package ca;

import app.automaton.RuleRegistry;
import app.automaton.ValueRegistry;
import ca.rules.Rule;
import ca.values.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

    private final String RULES_KEY = "rules", CELLS_KEY = "cells";
    private final PyObject classesList;

    private boolean rulesLoaded = false, cellsLoaded = false;

    public PythonLoader(FileHandle filename) {
        interpreter = new PythonInterpreter();
        interpreter.execfile(filename.path());

        classesList = interpreter.get("get_classes").__call__();
    }

    public void loadRules() {
        if (!rulesLoaded) {
            Vector<Rule<?>> rulesVec = new Vector<>();
            PyListIterator iterator = new PyListIterator((PyList) classesList.__getitem__(new PyString(RULES_KEY)));
            iterator.forEach( t -> {
                try {
                    Rule<?> rule = ((Class<Rule>) t).getConstructor().newInstance();
                    RuleRegistry.registerRule(rule);
                    rulesVec.add(rule);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    Gdx.app.error("PYTHON LOAD", e.getMessage(), e);
                }
            });

            rulesLoaded = true;
        }
    }

    public void loadValues() {
        if (!cellsLoaded) {
            Vector<Class<? extends Value>> cellsVec = new Vector<>();
            PyListIterator iterator = new PyListIterator((PyList) classesList.__getitem__(new PyString(CELLS_KEY)));
            iterator.forEach( t -> {
                try{
                    ValueRegistry.registerValue((Class<? extends Value>) t);
                    cellsVec.add((Class<? extends Value>) t);
                } catch (Exception e) {
                    Gdx.app.error("PYTHON LOAD", e.getMessage(), e);
                }
            });

            cellsLoaded = true;
        }
    }

}
