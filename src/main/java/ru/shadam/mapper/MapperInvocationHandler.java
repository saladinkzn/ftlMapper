package ru.shadam.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * InvocationHandler for our "Repository"
 *
 * @author Timur Shakurov
 */
public class MapperInvocationHandler implements InvocationHandler {
    private QueryManager queryManager;

    private DataSourceAdapter dataSourceAdapter;

    public MapperInvocationHandler(QueryManager queryManager, DataSourceAdapter dataSourceAdapter) {
        this.queryManager = queryManager;
        this.dataSourceAdapter = dataSourceAdapter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Query query = method.getAnnotation(Query.class);
        // Currently methods not annotated by @Query are not supported
        if(query == null) {
            // TODO: toString? getClass?
            throw new UnsupportedOperationException("invocation handler does not support non-Query methods");
        }
        final String templateName = query.templateName();
        final RowMapper<?> rowMapper = query.mapper().newInstance();
        // Ищем параметры, отмеченные аннотацией @Param
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final HashMap<String, Object> params = new HashMap<>();
        for(int i = 0; i < args.length; i++ ){
            final Annotation[] annotations = parameterAnnotations[i];
            for(Annotation annotation: annotations) {
                if(annotation instanceof Param) {
                    Param param = (Param) annotation;
                    final String paramName = param.value();
                    params.put(paramName, args[i]);
                }
            }
        }
        final String sql = queryManager.getQuery(templateName, params);
        //
        final Class<?> returnType = method.getReturnType();
        // TODO: improve type support
        if(returnType.isAssignableFrom(List.class)) {
            return dataSourceAdapter.query(sql, rowMapper);
        } else {
            return dataSourceAdapter.uniqueQuery(sql, rowMapper);
        }
    }
}
