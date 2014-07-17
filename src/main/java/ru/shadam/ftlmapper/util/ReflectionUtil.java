package ru.shadam.ftlmapper.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class ReflectionUtil {

    public static final int SET_PREFIX_LENGTH = "set".length();

    /**
     * checks if method is setter (void setXXX(T param);)
     * @param method
     * @return
     */
    private static boolean isSetter(Method method) {
        return method.getReturnType().equals(void.class)
                && method.getName().startsWith("set")
                && method.getParameterTypes().length == 1;
    }

    /**
     * return all setters for class
     * @param clazz
     * @return
     */
    public static List<Method> getSetters(Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        List<Method> result = new ArrayList<>();
        for(Method method: methods) {
            if (isSetter(method)) {
                result.add(method);
            }
        }
        return result;
    }

    /**
     * return "propertyName" from setter.
     * @param setter
     * @return
     */
    public static String getPropertyName(Method setter) {
        if(!isSetter(setter)) {
            throw new IllegalArgumentException("method should be setter");
        }
        final String capitalized = setter.getName().substring(SET_PREFIX_LENGTH);
        return capitalized.substring(0, 1).toLowerCase() + capitalized.substring(1);
    }

    public static Map<String, Method> getPropertyMap(Class<?> clazz) {
        final List<Method> setters = getSetters(clazz);
        final Map<String, Method> result = new HashMap<>();
        for(Method setter: setters) {
            result.put(getPropertyName(setter), setter);
        }
        return result;
    }
}
