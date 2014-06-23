package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.AnnotationRowMapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.mapper.single.*;
import ru.shadam.ftlmapper.query.annotations.*;
import ru.shadam.ftlmapper.query.query.RawQueryStrategy;
import ru.shadam.ftlmapper.query.query.TemplateQueryStrategy;
import ru.shadam.ftlmapper.query.result.ListResultStrategy;
import ru.shadam.ftlmapper.query.result.UniqueResultStrategy;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            if(methodReturnType.isAssignableFrom(List.class)) {
                final Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                    final Class<?> typeParameter = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    rowMapper = new AnnotationRowMapper<>(typeParameter);
                } else {
                    throw new IllegalArgumentException("unexpected generic return type: " + genericReturnType);
                }
            } else {
                if(methodReturnType.isAnnotationPresent(MappedType.class)) {
                    rowMapper = new AnnotationRowMapper<>(methodReturnType);
                } else {
                    if(Long.class.equals(methodReturnType) || long.class.equals(methodReturnType)) {
                        rowMapper = new SingleLongColumnRowMapper(Long.class.equals(methodReturnType));
                    } else if (Integer.class.equals(methodReturnType) || int.class.equals(methodReturnType)) {
                        rowMapper = new SingleIntegerColumnRowMapper(Integer.class.equals(methodReturnType));
                    } else if (Short.class.equals(methodReturnType) || short.class.equals(methodReturnType)) {
                        rowMapper = new SingleShortColumnRowMapper(Short.class.equals(methodReturnType));
                    } else if (Byte.class.equals(methodReturnType) || byte.class.equals(methodReturnType)) {
                        rowMapper = new SingleByteColumnRowMapper(Short.class.equals(methodReturnType));
                    } else if (String.class.equals(methodReturnType)) {
                        rowMapper = new SingleStringColumnRowMapper();
                    } else {
                        throw new IllegalArgumentException("Result must be @MappedType-annotated class");
                    }
                }
            }
        }
        //
        parameterAnnotations = method.getParameterAnnotations();
        //
        if(methodReturnType.isAssignableFrom(List.class)) {
            resultStrategy = new ListResultStrategy<>(rowMapper);
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
