package ru.shadam.extractor.inner.object;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * @author sala
 */
public class ConstructorInstanceProvider<T> implements InstanceProvider<T> {
    private final Constructor<T> constructor;
    private final List<String> getNames;

    public ConstructorInstanceProvider(Constructor<T> constructor, List<String> getNames) {
        if(!Modifier.isPublic(constructor.getModifiers())) {
            throw new IllegalArgumentException("constructor should be accessible");
        }
        this.constructor = constructor;
        this.getNames = getNames;
    }

    @Override
    public T newInstance(Map<String, Object> attributes) {
        try {
            final Object[] args = new Object[getNames.size()];
            int i = 0;
            for(String getName: getNames) {
                final Object getValue = attributes.get(getName);
                args[i++] = getValue;
            }
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException e) {
            // should not happen
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            // unwrapping and rethrowing
            throw new RuntimeException(e.getCause());
        }
    }
}
