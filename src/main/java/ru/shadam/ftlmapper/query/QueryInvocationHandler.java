package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import ru.shadam.ftlmapper.mapper.MapperInvocationHandler;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Mapper;
import ru.shadam.ftlmapper.query.annotations.Param;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;

/**
 * InvocationHandler for our "Repository"
 *
 * @author Timur Shakurov
 */
public class QueryInvocationHandler implements InvocationHandler {
    private QueryManager queryManager;

    private DataSourceAdapter dataSourceAdapter;

    public QueryInvocationHandler(QueryManager queryManager, DataSourceAdapter dataSourceAdapter) {
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
        final String templateName = query.value();
        final RowMapper<?> rowMapper;
        final Mapper mapper = method.getAnnotation(Mapper.class);
        final MappedType mappedType = method.getAnnotation(MappedType.class);
        if(mapper != null) {
            rowMapper = mapper.value().newInstance();
        } else if (mappedType != null) {
            rowMapper = RowMapper.class.cast(Proxy.newProxyInstance(
                    RowMapper.class.getClassLoader(),
                    new Class[]{RowMapper.class},
                    new MapperInvocationHandler(mappedType.value())
            ));
        } else {
            throw new IllegalStateException();
        }
        // Ищем параметры, отмеченные аннотацией @Param
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final HashMap<String, Object> params = new HashMap<>();
        if(args != null) {
            for (int i = 0; i < args.length; i++) {
                final Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Param) {
                        Param param = (Param) annotation;
                        final String paramName = param.value();
                        params.put(paramName, args[i]);
                    }
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
