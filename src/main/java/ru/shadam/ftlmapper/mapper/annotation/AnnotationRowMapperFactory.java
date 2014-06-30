package ru.shadam.ftlmapper.mapper.annotation;

import ru.shadam.ftlmapper.annotations.mapper.Creator;
import ru.shadam.ftlmapper.annotations.mapper.Embedded;
import ru.shadam.ftlmapper.annotations.mapper.Property;
import ru.shadam.ftlmapper.annotations.query.MappedType;
import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sala
 */
public class AnnotationRowMapperFactory {
    public <T> AnnotationRowMapper<T> annotationRowMapper(String prefix, Class<T> clazz) {
        NewInstanceSupplier<T> creator = getNewInstanceCreator(clazz);
        //
        final List<ResultSetConsumer<T>> consumers = new ArrayList<>();
        //
        final List<Field> fieldList = collectFields(clazz);
        for(final Field field: fieldList) {
            consumers.add(getResultSetConsumer(prefix, clazz, field));
        }
        return new AnnotationRowMapper<>(creator, consumers);
    }

    private <T> List<Field> collectFields(Class<T> clazz) {
        final List<Field> fieldList = new ArrayList<>();
        for(Class<? super T> ancestor = clazz; ancestor != null; ancestor = ancestor.getSuperclass()) {
            if(ancestor.isAnnotationPresent(MappedType.class)) {
                final Field[] fields = ancestor.getDeclaredFields();
                Collections.addAll(fieldList, fields);
            }
        }
        return fieldList;
    }

    private <T> ResultSetConsumer<T> getResultSetConsumer(String prefix, Class<T> clazz, final Field field) {
        final Property property = field.getAnnotation(Property.class);
        final Embedded embedded = field.getAnnotation(Embedded.class);
        if((property == null && embedded == null) || (property != null && embedded != null)) {
            throw new IllegalArgumentException("Field cannot have nor Property or Embedded annotation neither both");
        }
        final ResultSetConsumer<T> resultSetConsumer;
        if(property != null) {
            field.setAccessible(true);
            final String annotatedName = property.value();
            final String propertyName = prefix + (annotatedName.isEmpty() ? field.getName() : annotatedName);
            resultSetConsumer = new ResultSetConsumer<T>() {
                @Override
                public void consume(T instance, ResultSetWrapper resultSetRow) throws SQLException {
                    try {
                        field.set(instance, resultSetRow.getObject(propertyName));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("should not happen");
                    }
                }
            };
        } else {
            field.setAccessible(true);
            final String embeddedPrefix = prefix + embedded.value();
            final Class<?> embeddedClass = field.getType();
            if(!clazz.isAnnotationPresent(MappedType.class)) {
                throw new IllegalStateException("Field mapped with @Embedded should be of class mapped with @MappedType");
            }
            final AnnotationRowMapper rowMapper2 = annotationRowMapper(embeddedPrefix, embeddedClass);
            resultSetConsumer = new ResultSetConsumer<T>() {
                @Override
                public void consume(T instance, ResultSetWrapper resultSetRow) throws SQLException {
                    try {
                        field.set(instance, rowMapper2.mapRow(resultSetRow));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("should not happen");
                    }
                }
            };
        }
        return resultSetConsumer;
    }

    private <T> NewInstanceSupplier<T> getNewInstanceCreator(Class<T> clazz) {
        NewInstanceSupplier<T> creator = null;
        final Constructor<?>[] constructors = clazz.getConstructors();
        for(final Constructor<?> constructor: constructors) {
            if(constructor.isAnnotationPresent(Creator.class)) {
                if(creator != null) {
                    throw new IllegalArgumentException("Only one constructor can be mapped by @Creator");
                }
                final Class<?>[] parameterClasses = constructor.getParameterTypes();
                final Annotation[][] annotations = constructor.getParameterAnnotations();
                final RowMapper[] argProperties = new RowMapper[annotations.length];
                for(int paramIndex = 0; paramIndex < annotations.length; paramIndex++) {
                    RowMapper rowMapper = null;
                    for(final Annotation annotation: annotations[paramIndex]) {
                        if(annotation instanceof Property) {
                            rowMapper = new RowMapper() {
                                @Override
                                public Object mapRow(ResultSetWrapper resultSet) throws SQLException {
                                    return resultSet.getObject(((Property) annotation).value());
                                }
                            };
                        }
                        if(annotation instanceof Embedded) {
                            final String argPrefix = ((Embedded) annotation).value();
                            rowMapper = annotationRowMapper(argPrefix, parameterClasses[paramIndex]);
                        }
                    }
                    if(rowMapper == null) {
                        throw new IllegalArgumentException("Each argument of @Creator should be mapped with @Property or @Embedded");
                    }
                    argProperties[paramIndex] = rowMapper;
                }
                creator = new NewInstanceSupplier<T>() {
                    @Override
                    public T newInstance(ResultSetWrapper resultSetWrapper) throws SQLException {
                        final Object[] argValues = new Object[argProperties.length];
                        for(int i = 0; i < argProperties.length; i++) {
                            final Object value = argProperties[i].mapRow(resultSetWrapper);
                            argValues[i] = value;
                        }
                        try {
                            return (T) constructor.newInstance(argValues);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        }
        if(creator == null) {
            try {
                final Constructor<T> defaultCtor = clazz.getConstructor();
                creator = new NewInstanceSupplier<T>() {
                    @Override
                    public T newInstance(ResultSetWrapper resultSetWrapper) throws SQLException {
                        try {
                            return defaultCtor.newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("If class has no consturctor mapped by @Creator");
            }
        }
        return creator;
    }
}
