package ru.shadam.ftlmapper.extractor.inner.object;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author sala
 */
public class FieldInstanceFiller<T> implements InstanceFiller<T> {
    private final Field field;
    private final String getName;

    public FieldInstanceFiller(Field field, String getName) {
        if(!Modifier.isPublic(field.getModifiers())) {
            throw new IllegalArgumentException("field must be accessible");
        }
        this.field = field;
        this.getName = getName;
    }

    @Override
    public void fillInstance(T instance, Map<String, Object> attributes) {
        try {
            field.set(instance, attributes.get(getName));
        } catch (IllegalAccessException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }
}
