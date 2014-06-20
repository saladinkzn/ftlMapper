package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.AnnotationRowMapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Mapper;
import ru.shadam.ftlmapper.query.annotations.Param;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class MethodEvaluationInfo {
    private final ResultStrategy resultStrategy;
    private final String templateName;
    private final RowMapper<?> rowMapper;
    private final Annotation[][] parameterAnnotations;
    //

    public MethodEvaluationInfo(Method method) {
        final Query query = method.getAnnotation(Query.class);
        if(query == null) {
            throw new IllegalArgumentException("Only methods annotated with @Query is supported");
        }
        this.templateName = query.value();
        //
        final Mapper mapper = method.getAnnotation(Mapper.class);
        final MappedType mappedType = method.getAnnotation(MappedType.class);
        if(mappedType == null && mapper == null) {
            throw new IllegalArgumentException("method should be annotated with @Mapper or @MappedType");
        }
        //
        if(mappedType != null && mapper != null) {
            throw new IllegalArgumentException("method should not be annotated with both @Mapper and @MappedType");
        }
        //
        if(mapper != null) {
            try {
                rowMapper = mapper.value().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("If method is annotated with @Mapper annotation this class should be instantiable with empty constructor");
            }
        } else {
            rowMapper = new AnnotationRowMapper<>(mappedType.value());
        }
        //
        parameterAnnotations = method.getParameterAnnotations();
        //

        if(method.getReturnType().isAssignableFrom(List.class)) {
            resultStrategy = new ListResultStrategy();
        } else {
            resultStrategy = new UniqueResultStrategy();
        }

    }

    public Map<String, Object> getParameters(Object[] args) {
        final Map<String, Object> params = new HashMap<>();
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

    public String getTemplateName() {
        return templateName;
    }

    public RowMapper<?> getRowMapper() {
        return rowMapper;
    }

    public ResultStrategy getResultStrategy() {
        return resultStrategy;
    }
}
