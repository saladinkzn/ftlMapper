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
        //
        final Class<?> methodReturnType = method.getReturnType();
        final RowMapper<?> rowMapper;
        if(mapper != null) {
            try {
                rowMapper = mapper.value().newInstance();
                // TODO
                if(methodReturnType.equals(List.class)) {
                    return new ListResultSetExtractor<>(rowMapper);
                } else if (methodReturnType.equals(Set.class)) {
                    return new SetResultSetExtractor<>(rowMapper);
                } else if (methodReturnType.equals(Map.class)) {
                    final ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
                    if(!method.isAnnotationPresent(KeyExtractor.class)) {
                        throw new IllegalArgumentException("Method which returns Map should be annotated with @KeyExtractor");
                    }
                    final KeyExtractor keyExtractor = method.getAnnotation(KeyExtractor.class);
                    final String keyExtractorValue = keyExtractor.value();
                    final Type keyType = genericReturnType.getActualTypeArguments()[0];
                    final RowMapper keyMapper = rowMapperFactory.getRowMapper(keyType, keyExtractorValue);
                    return new MapResultSetExtractor<>(keyMapper, rowMapper);
                } else if (methodReturnType.isArray()) {
                    return new ArrayResultSetExtractor(rowMapper, methodReturnType);
                } else {
                    return new UniqueResultExtractor<>(rowMapper);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("If method is annotated with @Mapper annotation this class should be instantiable with empty constructor");
            }
        } else {
            final Type classToMap;
            if(methodReturnType.equals(List.class) || methodReturnType.equals(Set.class) || methodReturnType.equals(Map.class)) {
                final Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                    final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    classToMap = actualTypeArguments[0];
                    if(methodReturnType.equals(List.class)) {
                        return new ListResultSetExtractor<>(rowMapperFactory.getRowMapper(classToMap));
                    } else if (methodReturnType.equals(Map.class)) {
                        final KeyExtractor keyExtractor = method.getAnnotation(KeyExtractor.class);
                        if(keyExtractor == null) {
                            throw new IllegalArgumentException("Map-returning method should be annotated with @KeyExtractor");
                        }
                        final RowMapper<?> keyMapper = rowMapperFactory.getRowMapper(classToMap, keyExtractor.value());
                        final RowMapper<?> valueMapper = rowMapperFactory.getRowMapper(actualTypeArguments[1]);
                        return new MapResultSetExtractor<>(keyMapper, valueMapper);
                    } else {
                        return new SetResultSetExtractor<>(rowMapperFactory.getRowMapper(classToMap));
                    }
                } else {
                    throw new IllegalArgumentException("unexpected generic return type: " + genericReturnType);
                }
            } else if(methodReturnType.isArray()) {
                classToMap = methodReturnType.getComponentType();
                return new ArrayResultSetExtractor(rowMapperFactory.getRowMapper(classToMap), ((Class<?>) classToMap));
            } else {
                classToMap = methodReturnType;
                return new UniqueResultExtractor<>(rowMapperFactory.getRowMapper(classToMap));
            }
        }
    }
}
