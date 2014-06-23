package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.AnnotationRowMapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.mapper.single.*;
import ru.shadam.ftlmapper.query.annotations.*;
import ru.shadam.ftlmapper.query.query.RawQueryStrategy;
import ru.shadam.ftlmapper.query.query.TemplateQueryStrategy;
import ru.shadam.ftlmapper.query.result.CollectionResultStrategy;
import ru.shadam.ftlmapper.query.result.UniqueResultStrategy;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Timur Shakurov
 */
public class MethodEvaluationInfo {
    private final ResultStrategy resultStrategy;
    private final QueryStrategy queryStrategy;
    private final Annotation[][] parameterAnnotations;
    //

    public MethodEvaluationInfo(QueryManager queryManager, Method method) {

        final Template template = method.getAnnotation(Template.class);
        if(template != null) {
            this.queryStrategy = new TemplateQueryStrategy(queryManager, template.value());
        } else {
            final Query query = method.getAnnotation(Query.class);
            if(query != null) {
                this.queryStrategy = new RawQueryStrategy(query.value());
            } else {
                throw new IllegalArgumentException("Either @Query or @Template annotation must be specified");
            }
        }
        //
        final Mapper mapper = method.getAnnotation(Mapper.class);
        //
        final Class<?> methodReturnType = method.getReturnType();
        final RowMapper<?> rowMapper;
        if(mapper != null) {
            try {
                rowMapper = mapper.value().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("If method is annotated with @Mapper annotation this class should be instantiable with empty constructor");
            }
        } else {
            final Class<?> classToMap;
            if(methodReturnType.equals(List.class) || methodReturnType.equals(Set.class)) {
                final Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                    classToMap = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                } else {
                    throw new IllegalArgumentException("unexpected generic return type: " + genericReturnType);
                }
            } else {
                classToMap = methodReturnType;
            }
            if(classToMap.isAnnotationPresent(MappedType.class)) {
                rowMapper = new AnnotationRowMapper<>(classToMap);
            } else {
                if(Long.class.equals(classToMap) || long.class.equals(classToMap)) {
                    rowMapper = new SingleLongColumnRowMapper(Long.class.equals(classToMap));
                } else if (Integer.class.equals(classToMap) || int.class.equals(classToMap)) {
                    rowMapper = new SingleIntegerColumnRowMapper(Integer.class.equals(classToMap));
                } else if (Short.class.equals(classToMap) || short.class.equals(classToMap)) {
                    rowMapper = new SingleShortColumnRowMapper(Short.class.equals(classToMap));
                } else if (Byte.class.equals(classToMap) || byte.class.equals(classToMap)) {
                    rowMapper = new SingleByteColumnRowMapper(Short.class.equals(classToMap));
                } else if (String.class.equals(classToMap)) {
                    rowMapper = new SingleStringColumnRowMapper();
                } else {
                    throw new IllegalArgumentException("Result must be @MappedType-annotated class");
                }
            }
        }
        //
        parameterAnnotations = method.getParameterAnnotations();
        //
        if(CollectionResultStrategy.isSupported(methodReturnType)) {
            resultStrategy = new CollectionResultStrategy<>(methodReturnType, rowMapper);
        } else {
            resultStrategy = new UniqueResultStrategy<>(rowMapper);
        }

    }

    public Map<String, Object> getParameters(Object[] args) {
        final Map<String, Object> params = new LinkedHashMap<>();
        if(args == null) {
            return params;
        }
        assert args.length == parameterAnnotations.length;
        for(int i = 0; i < args.length; i++) {
            for(Annotation annotation: parameterAnnotations[i]) {
                if(annotation instanceof Param) {
                    final String parameterName = ((Param) annotation).value();
                    final Object value = args[i];
                    params.put(parameterName, value);
                }
            }
        }
        return params;
    }

    public QueryStrategy getQueryStrategy() {
        return queryStrategy;
    }

    public ResultStrategy getResultStrategy() {
        return resultStrategy;
    }
}
