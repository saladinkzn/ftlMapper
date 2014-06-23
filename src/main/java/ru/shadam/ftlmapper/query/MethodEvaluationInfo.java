package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.AnnotationRowMapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.annotations.*;
import ru.shadam.ftlmapper.query.query.RawQueryStrategy;
import ru.shadam.ftlmapper.query.query.TemplateQueryStrategy;
import ru.shadam.ftlmapper.query.result.ListResultStrategy;
import ru.shadam.ftlmapper.query.result.UniqueResultStrategy;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class MethodEvaluationInfo {
    private final QueryManager queryManager;
    private final ResultStrategy resultStrategy;
    private final QueryStrategy queryStrategy;
    private final Annotation[][] parameterAnnotations;
    //

    public MethodEvaluationInfo(QueryManager queryManager, Method method) {
        this.queryManager = queryManager;

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
        final MappedType mappedType = method.getAnnotation(MappedType.class);
        if(mappedType == null && mapper == null) {
            throw new IllegalArgumentException("method should be annotated with @Mapper or @MappedType");
        }
        //
        if(mappedType != null && mapper != null) {
            throw new IllegalArgumentException("method should not be annotated with both @Mapper and @MappedType");
        }
        //
        final RowMapper<?> rowMapper;
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
            resultStrategy = new ListResultStrategy<>(rowMapper);
        } else {
            resultStrategy = new UniqueResultStrategy<>(rowMapper);
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

    public QueryStrategy getQueryStrategy() {
        return queryStrategy;
    }

    public ResultStrategy getResultStrategy() {
        return resultStrategy;
    }
}
