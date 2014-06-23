package ru.shadam.ftlmapper.mapper;

import ru.shadam.ftlmapper.mapper.annotations.Property;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Timur Shakurov
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {
    private Class<T> mappedType;
    private Map<String, Field> fields;
    private Set<String> properties;

    public AnnotationRowMapper(Class<T> mappedType) {
        this.mappedType = mappedType;
        properties = new HashSet<>();
        fields = new HashMap<>();
        //
        final Field[] fields = mappedType.getDeclaredFields();
        for(Field field: fields) {
            final Property annotation = field.getAnnotation(Property.class);
            if(annotation != null) {
                field.setAccessible(true);
                final String annotatedName = annotation.value();
                final String propertyName = annotatedName.isEmpty() ? field.getName() : annotatedName;
                properties.add(propertyName);
                this.fields.put(propertyName, field);
            }
        }
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        try {
            final Map<String, Object> values = new HashMap<>();
            for (String propertyName : properties) {
                final Object value = resultSet.getObject(propertyName);
                values.put(propertyName, value);
            }
            final Object instance = mappedType.newInstance();
            for (String property : values.keySet()) {
                fields.get(property).set(instance, values.get(property));
            }
            return mappedType.cast(instance);
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO: check to prevent.
            throw new RuntimeException(e);
        }
    }
}
