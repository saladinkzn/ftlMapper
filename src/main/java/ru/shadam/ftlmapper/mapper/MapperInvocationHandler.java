package ru.shadam.ftlmapper.mapper;

import ru.shadam.ftlmapper.mapper.annotations.Property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class MapperInvocationHandler implements InvocationHandler {
    private Class<?> mappedType;
    private Map<String, Field> fields;
    private Map<String, Class<?>> properties;

    public MapperInvocationHandler(Class<?> mappedType) {
        this.mappedType = mappedType;
        properties = new HashMap<>();
        fields = new HashMap<>();

        final Field[] fields = mappedType.getDeclaredFields();
        for(Field field: fields) {
            final Property annotation = field.getAnnotation(Property.class);
            if(annotation != null) {
                field.setAccessible(true);
                final String propertyName = annotation.value();
                final Class<?> clazz = field.getType();
                properties.put(propertyName, clazz);
                this.fields.put(propertyName, field);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.getName().equals("mapRow")) {
            throw new UnsupportedOperationException();
        }
        final ResultSet resultSet = (ResultSet)args[0];
        final Map<String, Object> values = new HashMap<>();
        for(Map.Entry<String, Class<?>> property: properties.entrySet()) {
            final String propertyName = property.getKey();
            final Object value = resultSet.getObject(propertyName);
            values.put(propertyName, value);
        }
        final Object instance = mappedType.newInstance();
        for(String property: values.keySet()) {
            fields.get(property).set(instance, values.get(property));
        }
        return mappedType.cast(instance);
    }
}
