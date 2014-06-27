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
    private static class BaseEmbeddedInfo {
        public final String prefix;
        public final Class<?> clazz;

        public BaseEmbeddedInfo(String prefix, Class<?> clazz) {
            this.prefix = prefix;
            this.clazz = clazz;
        }
    }

    private static class EmbeddedFieldInfo extends BaseEmbeddedInfo {
        public final Field field;
        //

        private EmbeddedFieldInfo(String prefix, Class<?> clazz, Field field) {
            super(prefix, clazz);
            this.field = field;
        }
    }

    private static class EmbeddedParamInfo extends BaseEmbeddedInfo {
        public final int index;

        private EmbeddedParamInfo(String prefix, Class<?> clazz, int index) {
            super(prefix, clazz);
            this.index = index;
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
        final List<EmbeddedFieldInfo> embeddeds = new ArrayList<>();
        //
        final List<Field> fieldList = new ArrayList<>();
        for(Class<? super T> ancestor = mappedType; ancestor != null; ancestor = ancestor.getSuperclass()) {
            if(ancestor.isAnnotationPresent(MappedType.class)) {
                final Field[] fields = ancestor.getDeclaredFields();
                Collections.addAll(fieldList, fields);
            }
        }
        for(Field field: fieldList) {
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
                embeddeds.add(new EmbeddedFieldInfo(embeddedPrefix, clazz, field));
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
                final List<EmbeddedParamInfo> embeddedParamInfos = new ArrayList<>();
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    boolean properlyAnnotated = false;
                    for(Annotation annotation: parameterAnnotations[i]) {
                        if(annotation instanceof Property) {
                            properlyAnnotated = true;
                            final Property param = ((Property) annotation);
                            final String paramName = param.value();
                            if(paramName.isEmpty()) {
                                throw new IllegalArgumentException("constructor arg mapped with @Property must have value");
                            }
                            creatorArgs[i] = prefix + paramName;
                            break;
                        }
                        if(annotation instanceof Embedded) {
                            properlyAnnotated = true;
                            final Embedded embedded = ((Embedded) annotation);
                            final String embeddedPrefix = embedded.value();
                            final Class<?> embeddedType = constructor.getParameterTypes()[i];
                            final EmbeddedParamInfo embeddedParamInfo = new EmbeddedParamInfo(embeddedPrefix, embeddedType, i);
                            embeddedParamInfos.add(embeddedParamInfo);
                        }
                    }
                    if(!properlyAnnotated) {
                        throw new IllegalArgumentException("@Creator constructor must have all parameters mapped with @Property");
                    }
                }
                if(creationStrategy == null) {
                    creationStrategy = new AnnotatedConstructorCreationStrategy<T>(constructor, creatorArgs, properties, fieldMap, embeddeds, embeddedParamInfos);
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
        private final List<EmbeddedFieldInfo> embeddeds;

        private DefaultCreationStrategy(Constructor<T> constructor, Map<String, Field> fieldMap, Set<String> properties, List<EmbeddedFieldInfo> embeddeds) {
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
            for(EmbeddedFieldInfo embeddedInfo : embeddeds) {
                final AnnotationRowMapper<?> rowMapper = new AnnotationRowMapper<>(embeddedInfo.prefix, embeddedInfo.clazz);
                final Object value = rowMapper.mapRow(resultSet);
                embeddedInfo.field.set(instance, value);
            }
        }
    }

    public static class AnnotatedConstructorCreationStrategy<T> extends DefaultCreationStrategy<T> {
        private final String[] constructorParams;
        private final List<EmbeddedParamInfo> embeddedParamInfos;

        //
        public AnnotatedConstructorCreationStrategy(Constructor<T> constructor, String[] constructorParams, Set<String> properties, Map<String, Field> fieldMap, List<EmbeddedFieldInfo> embeddeds, List<EmbeddedParamInfo> embeddedParamInfos) {
            super(constructor, fieldMap, properties, embeddeds);
            this.constructorParams = constructorParams;
            this.embeddedParamInfos = embeddedParamInfos;
        }

        @Override
        public T create(ResultSet resultSet) throws SQLException {
            final Object[] args = new Object[constructorParams.length];
            for (int i = 0; i < constructorParams.length; i++) {
                final String constructorParam = constructorParams[i];
                if(constructorParam != null) {
                    final Object value = resultSet.getObject(constructorParam);
                    args[i] = value;
                }
            }
            for(EmbeddedParamInfo embeddedParamInfo: embeddedParamInfos) {
                final String embeddedPrefix = embeddedParamInfo.prefix;
                final Class<?> embeddedType = embeddedParamInfo.clazz;
                final Object value = new AnnotationRowMapper<>(embeddedPrefix, embeddedType).mapRow(resultSet);
                args[embeddedParamInfo.index] = value;
            }
            //
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
