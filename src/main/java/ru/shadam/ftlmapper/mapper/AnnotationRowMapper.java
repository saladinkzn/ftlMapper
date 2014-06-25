package ru.shadam.ftlmapper.mapper;

import ru.shadam.ftlmapper.mapper.annotations.Creator;
import ru.shadam.ftlmapper.mapper.annotations.Embedded;
import ru.shadam.ftlmapper.mapper.annotations.Property;
import ru.shadam.ftlmapper.query.annotations.MappedType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Timur Shakurov
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {
    private static class EmbeddedInfo {
        private final String prefix;
        private final Class<?> clazz;
        private final Field field;
        //

        private EmbeddedInfo(String prefix, Class<?> clazz, Field field) {
            this.prefix = prefix;
            this.clazz = clazz;
            this.field = field;
        }
    }

    private CreationStrategy<T> creationStrategy;

    public AnnotationRowMapper(Class<T> mappedType) {
        this("", mappedType);
    }

    public AnnotationRowMapper(String prefix, Class<T> mappedType) {
        final Map<String, Field> fieldMap = new HashMap<>();
        //
        final Set<String> properties = new HashSet<>();
        //
        final List<EmbeddedInfo> embeddeds = new ArrayList<>();
        //
        final Field[] fields = mappedType.getDeclaredFields();
        for(Field field: fields) {
            final Property property = field.getAnnotation(Property.class);
            if(property != null) {
                field.setAccessible(true);
                final String annotatedName = property.value();
                final String propertyName = prefix + (annotatedName.isEmpty() ? field.getName() : annotatedName);
                properties.add(propertyName);
                fieldMap.put(propertyName, field);
            }
            final Embedded embedded = field.getAnnotation(Embedded.class);
            if(embedded != null) {
                field.setAccessible(true);
                final String embeddedPrefix = prefix + embedded.value();
                final Class<?> clazz = field.getType();
                if(!clazz.isAnnotationPresent(MappedType.class)) {
                    throw new IllegalStateException("Field mapped with @Embedded should be of class mapped with @MappedType");
                }
                embeddeds.add(new EmbeddedInfo(embeddedPrefix, clazz, field));
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
                            creatorArgs[i] = prefix + paramName;
                            break;
                        }
                    }
                    if(!annotatedWithProperty) {
                        throw new IllegalArgumentException("@Creator constructor must have all parameters mapped with @Property");
                    }
                }
                if(creationStrategy == null) {
                    creationStrategy = new AnnotatedConstructorCreationStrategy<T>(constructor, creatorArgs, properties, fieldMap, embeddeds);
                } else {
                    throw new IllegalArgumentException("Only one constructor can be mapped with @Creator");
                }
            }
        }
        if(creationStrategy == null) {
            try {
                final Constructor<T> defaultConstructor = mappedType.getConstructor();
                creationStrategy = new DefaultCreationStrategy<>(defaultConstructor, fieldMap, properties, embeddeds);
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
        private final List<EmbeddedInfo> embeddeds;

        private DefaultCreationStrategy(Constructor<T> constructor, Map<String, Field> fieldMap, Set<String> properties, List<EmbeddedInfo> embeddeds) {
            this.constructor = constructor;
            this.fieldMap = fieldMap;
            this.properties = properties;
            this.embeddeds = embeddeds;
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
            //
            for(EmbeddedInfo embeddedInfo : embeddeds) {
                final AnnotationRowMapper<?> rowMapper = new AnnotationRowMapper<>(embeddedInfo.prefix, embeddedInfo.clazz);
                final Object value = rowMapper.mapRow(resultSet);
                embeddedInfo.field.set(instance, value);
            }
        }
    }

    public static class AnnotatedConstructorCreationStrategy<T> extends DefaultCreationStrategy<T> {
        private final String[] constructorParams;
        //
        public AnnotatedConstructorCreationStrategy(Constructor<T> constructor, String[] constructorParams, Set<String> properties, Map<String, Field> fieldMap, List<EmbeddedInfo> embeddeds) {
            super(constructor, fieldMap, properties, embeddeds);
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
