package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.mapper.RowMapperFactory;
import ru.shadam.ftlmapper.query.annotations.Mapper;
import ru.shadam.ftlmapper.query.extractors.ArrayResultSetExtractor;
import ru.shadam.ftlmapper.query.extractors.ListResultSetExtractor;
import ru.shadam.ftlmapper.query.extractors.SetResultSetExtractor;
import ru.shadam.ftlmapper.query.extractors.UniqueResultExtractor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
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
            if(methodReturnType.equals(List.class) || methodReturnType.equals(Set.class)) {
                final Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                    classToMap = parameterizedType.getActualTypeArguments()[0];
                    if(methodReturnType.equals(List.class)) {
                        return new ListResultSetExtractor<>(rowMapperFactory.getRowMapper(classToMap));
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
