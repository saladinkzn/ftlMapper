package ru.shadam.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * InvocationHandler for our "Repository"
 *
 * @author Timur Shakurov
 */
public class MapperInvocationHandler implements InvocationHandler {
    private DataSourceAdapter dataSourceAdapter;

    public MapperInvocationHandler(DataSourceAdapter dataSourceAdapter) {
        this.dataSourceAdapter = dataSourceAdapter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Query query = method.getAnnotation(Query.class);
        // Currently not annotated by @Query methods are not supported
        if(query == null) {
            // TODO: toString? getClass?
            throw new UnsupportedOperationException("invocation handler does not support non-Query methods");
        } else {
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
            return dataSourceAdapter.query(templateName, params, rowMapper);
        }
    }
}
