package ca.values;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ValueCollector {

    public static short[] collectValues(Class<? extends Cell> clazz) {
        try {
            Constructor<? extends Cell> constructor = clazz.getDeclaredConstructor();
            Cell cellInstance = constructor.newInstance();
            return cellInstance.getValues();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.printf("Class %s doesn't have public nullary constructor to create a value instance.\n", clazz.getName());
        } catch(InstantiationException e) {
            System.err.printf("Cannot instantiate class %s. Check if it is neither abstract not interface .\n", clazz.getName());
        }

        return null;
    }

}
