package ru.shadam.ftlmapper.mapper;

import ru.shadam.ftlmapper.mapper.annotations.Creator;
import ru.shadam.ftlmapper.mapper.annotations.Property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    private CreationStrategy<T> creationStrategy;

    public AnnotationRowMapper(Class<T> mappedType) {
        Set<String> properties = new HashSet<>();
        Map<String, Field> fieldMap = new HashMap<>();
        //
        final Field[] fields = mappedType.getDeclaredFields();
        for(Field field: fields) {
            final Property annotation = field.getAnnotation(Property.class);
            if(annotation != null) {
                field.setAccessible(true);
                final String annotatedName = annotation.value();
                final String propertyName = annotatedName.isEmpty() ? field.getName() : annotatedName;
                properties.add(propertyName);
                fieldMap.put(propertyName, field);
            }
        }
        //
        final Constructor<?>[] constructors = mappedType.getConstructors();
        CreationStrategy<T> creationStrategy = null;
        for(Constructor constructor: constructors) {
            final Creator creator = (Creator) constructor.getAnnotation(Creator.class);
            if(creator != null) {
                final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
                final String[] creatorArgs = new String[parameterAnnotations.length];
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    boolean annotatedWithProperty = false;
                    for(Annotation annotation: parameterAnnotations[i]) {
                        if(annotation instanceof Property) {
                            annotatedWithProperty = true;
                            final Property param = ((Property) annotation);
                            final String paramName = param.value();
                            if(paramName.isEmpty()) {
                                throw new IllegalArgumentException("constructor arg mapped with @Property must have value");
                            }
                            creatorArgs[i] = paramName;
                            break;
                        }
                    }
                    if(!annotatedWithProperty) {
                        throw new IllegalArgumentException("@Creator constructor must have all parameters mapped with @Property");
                    }
                }
                if(creationStrategy == null) {
                    creationStrategy = new AnnotatedConstructorCreationStrategy<T>(constructor, creatorArgs, properties, fieldMap);
                } else {
                    throw new IllegalArgumentException("Only one constructor can be mapped with @Creator");
                }
            }
        }
        if(creationStrategy == null) {
            try {
                final Constructor<T> defaultConstructor = mappedType.getConstructor();
                creationStrategy = new DefaultCreationStrategy<>(defaultConstructor, fieldMap, properties);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("if no constructor was mapped with @Creator there should be a default constructor");
            }
        }
        this.creationStrategy = creationStrategy;
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        return creationStrategy.create(resultSet);
    }

    private static interface CreationStrategy<T> {
        public T create(ResultSet resultSet) throws SQLException;
    }

    private static class DefaultCreationStrategy<T> implements CreationStrategy<T> {
        protected final Constructor<T> constructor;
        private final Map<String, Field> fieldMap;
        private final Set<String> properties;

        private DefaultCreationStrategy(Constructor<T> constructor, Map<String, Field> fieldMap, Set<String> properties) {
            this.constructor = constructor;
            this.fieldMap = fieldMap;
            this.properties = properties;
        }

        @Override
        public T create(ResultSet resultSet) throws SQLException {
            try {
                final T instance = constructor.newInstance();
                setMappedFields(resultSet, instance);
                return instance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        protected void setMappedFields(ResultSet resultSet, T instance) throws IllegalAccessException, SQLException {
            for(String property: properties) {
                final Object value = resultSet.getObject(property);
                final Field field = fieldMap.get(property);
                field.set(instance, value);
            }
        }
    }

    public static class AnnotatedConstructorCreationStrategy<T> extends DefaultCreationStrategy<T> {
        private final String[] constructorParams;
        //
        public AnnotatedConstructorCreationStrategy(Constructor<T> constructor, String[] constructorParams, Set<String> properties, Map<String, Field> fieldMap) {
            super(constructor, fieldMap, properties);
            this.constructorParams = constructorParams;
        }

        @Override
        public T create(ResultSet resultSet) throws SQLException {
            final Object[] args = new Object[constructorParams.length];
            for (int i = 0; i < constructorParams.length; i++) {
                final String constructorParam = constructorParams[i];
                final Object value = resultSet.getObject(constructorParam);
                args[i] = value;
            }
            try {
                final T instance = constructor.newInstance(args);
                setMappedFields(resultSet, instance);
                return instance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
