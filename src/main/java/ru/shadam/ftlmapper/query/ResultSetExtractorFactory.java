package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.annotations.query.KeyExtractor;
import ru.shadam.ftlmapper.annotations.query.Mapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.mapper.RowMapperFactory;
import ru.shadam.ftlmapper.query.extractors.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sala
 */
public class ResultSetExtractorFactory {
    private final RowMapperFactory rowMapperFactory;

    public ResultSetExtractorFactory(RowMapperFactory rowMapperFactory) {
        this.rowMapperFactory = rowMapperFactory;
    }

    public ResultSetExtractor getResultSetExtractor(Method method) {
        final Mapper mapper = method.getAnnotation(Mapper.class);
        final RowMapper<?> explicitMapper;
        try {
            explicitMapper = mapper == null ? null : mapper.value().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("If method is annotated with @Mapper annotation this class should be instantiable with empty constructor");
        }
        //
        final Class<?> methodReturnType = method.getReturnType();
        final Type classToMap;
        if(methodReturnType.equals(List.class) || methodReturnType.equals(Set.class) || methodReturnType.equals(Map.class)) {
            final Type genericReturnType = method.getGenericReturnType();
            if(genericReturnType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                classToMap = actualTypeArguments[0];
                if(methodReturnType.equals(List.class)) {
                    return new ListResultSetExtractor<>(coalesce(explicitMapper, classToMap));
                } else if (methodReturnType.equals(Map.class)) {
                    final KeyExtractor keyExtractor = method.getAnnotation(KeyExtractor.class);
                    if(keyExtractor == null) {
                        throw new IllegalArgumentException("Map-returning method should be annotated with @KeyExtractor");
                    }
                    final RowMapper<?> keyMapper = rowMapperFactory.getRowMapper(classToMap, keyExtractor.value());
                    final RowMapper<?> valueMapper = coalesce(explicitMapper, actualTypeArguments[1]);
                    return new MapResultSetExtractor<>(keyMapper, valueMapper);
                } else if (methodReturnType.equals(Set.class)) {
                    return new SetResultSetExtractor<>(coalesce(explicitMapper, classToMap));
                } else {
                    throw new IllegalArgumentException("Unknown method return type");
                }
            } else {
                throw new IllegalArgumentException("unexpected generic return type: " + genericReturnType);
            }
        } else if(methodReturnType.isAssignableFrom(Object[].class)) {
            return new UniqueResultExtractor<>(coalesce(explicitMapper, methodReturnType));
        } else if(methodReturnType.isArray()) {
            classToMap = methodReturnType.getComponentType();
            return new ArrayResultSetExtractor(coalesce(explicitMapper, classToMap), ((Class<?>) classToMap));
        } else {
            classToMap = methodReturnType;
            return new UniqueResultExtractor<>(coalesce(explicitMapper, classToMap));
        }
    }

    private RowMapper<?> coalesce(RowMapper mapper, Type type) {
        if(mapper != null) {
            return mapper;
        }
        return rowMapperFactory.getRowMapper(type);
    }
}
