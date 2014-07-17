package util;

import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.Creator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class ReflectionUtil {
    /**
     *
     * @param instance
     * @param fieldName
     * @param clazz
     * @param <T>
     * @param <U>
     * @return
     * @throws NoSuchFieldException
     * @throws ClassCastException if field's type is not of type U.
     */
    public static  <T, U> U getFieldValue(T instance, String fieldName, Class<? super T> clazz) throws NoSuchFieldException {
        final Field field = clazz.getDeclaredField(fieldName);
        try {
            field.setAccessible(true);
            return (U)field.get(instance);
        } catch (IllegalAccessException iaEx) {
            // Should not happen
            throw new RuntimeException(iaEx);
        }
    }

    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Constructor<T> getCreatorConstructor(Class<T> clazz) {
        final Constructor<?>[] constructors = clazz.getConstructors();
        for(Constructor<?> constructor: constructors) {
            if(constructor.isAnnotationPresent(Creator.class)) {
                return (Constructor<T>) constructor;
            }
        }
        throw new RuntimeException(new NoSuchMethodException("@Creator-annotated constructor was not found"));
    }

    public static List<String> getNames(Constructor<?> constructor) {
        final List<String> getNames = new ArrayList<>();
        final Annotation[][] parametersAnnotations = constructor.getParameterAnnotations();
        for(Annotation[] parameterAnnotations: parametersAnnotations) {
            String argName = null;
            for(Annotation parameterAnnotation: parameterAnnotations) {
                if(parameterAnnotation instanceof Column) {
                    final Column column = ((Column) parameterAnnotation);
                    if(column.value() != null && !column.value().isEmpty()) {
                        argName = column.value();
                    } else {
                        throw new IllegalArgumentException("All constructor's parameters must be annotated with @Column");
                    }
                }
            }
            if(argName == null) {
                throw new IllegalArgumentException("All constructor's parameters must be annotated with @Column");
            }
            getNames.add(argName);
        }
        return getNames;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getSetter(Class<?> clazz, String propertyName) {
        List<Method> setters = ru.shadam.ftlmapper.util.ReflectionUtil.getSetters(clazz);
        for(Method setter: setters) {
            if(propertyName.equals(ru.shadam.ftlmapper.util.ReflectionUtil.getPropertyName(setter))) {
                return setter;
            }
        }
        throw new RuntimeException("Not found");
    }
}
