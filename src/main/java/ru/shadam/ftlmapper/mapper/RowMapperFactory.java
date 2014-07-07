package ru.shadam.ftlmapper.mapper;

import ru.shadam.ftlmapper.annotations.query.MappedType;
import ru.shadam.ftlmapper.mapper.annotation.AnnotationRowMapperFactory;
import ru.shadam.ftlmapper.util.Function;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class RowMapperFactory {
    private Map<Class<?>, Function<String, RowMapper>> mapperMap;

    public RowMapperFactory() {
        mapperMap = new HashMap<>();
    }

    public boolean supports(Type type) {
        final Class<?> clazz;
        if(type instanceof Class<?>) {
            clazz = ((Class<?>) type);
        } else if(type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = ((ParameterizedType) type);
            clazz = ((Class<?>) parameterizedType.getRawType());
        } else {
            throw new IllegalArgumentException("Unexpected type: " + type);
        }
        return mapperMap.containsKey(clazz) || clazz.isAnnotationPresent(MappedType.class);
    }

    public RowMapper<?> getRowMapper(Type type) {
        return getRowMapper(type, "");
    }

    public RowMapper<?> getRowMapper(Type type, String nameOrPrefix) {
        final Class<?> clazz;
        final Type[] typeArguments;
        if(type instanceof Class<?>) {
            clazz = ((Class) type);
            typeArguments = null;
        } else {
            final ParameterizedType parameterizedType = ((ParameterizedType) type);
            // TODO: support deeper structure?
            clazz = (Class<?>) parameterizedType.getRawType();
            typeArguments = parameterizedType.getActualTypeArguments();
        }
        if(mapperMap.containsKey(clazz)) {
            return mapperMap.get(clazz).get(nameOrPrefix);
        }
        return AnnotationRowMapperFactory.getInstance(nameOrPrefix, clazz);
    }

    public <T> void register(Class<T> clazz, Function<String, RowMapper<? extends T>> mapper) {
        mapperMap.put(clazz, (Function)mapper);
    }
}
