package ru.shadam.extractor.inner.object;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author sala
 */
public class SetterInstanceFiller<T> implements InstanceFiller<T> {
    private final Method method;
    private final String attributeName;

    public SetterInstanceFiller(Method method, String attributeName) {
        if(!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalArgumentException("method should be accessible");
        }
        this.method = method;
        this.attributeName = attributeName;
    }

    @Override
    public void fillInstance(T instance, Map<String, Object> attributes) {
        try {
            method.invoke(instance, attributes.get(attributeName));
        } catch (IllegalAccessException e) {
            // should not happen
            throw new RuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
