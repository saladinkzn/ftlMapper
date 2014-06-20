package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InvocationHandler for our "Repository"
 *
 * @author Timur Shakurov
 */
public class QueryInvocationHandler implements InvocationHandler {
    private QueryManager queryManager;

    private DataSourceAdapter dataSourceAdapter;

    private Map<Method, MethodEvaluationInfo> methodInfo;

    public QueryInvocationHandler(Class<?> targetClass, QueryManager queryManager, DataSourceAdapter dataSourceAdapter) {
        this.queryManager = queryManager;
        this.dataSourceAdapter = dataSourceAdapter;
        //
        methodInfo = new HashMap<>();
        final Method[] methods = targetClass.getDeclaredMethods();
        for(Method method: methods) {
            try {
                final MethodEvaluationInfo methodEvaluationInfo = new MethodEvaluationInfo(method);
                methodInfo.put(method, methodEvaluationInfo);
            } catch (Exception ex) {
                // TODO: log
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!methodInfo.containsKey(method)) {
            throw new UnsupportedOperationException("method is not supported");
        }
        //
        final MethodEvaluationInfo methodEvaluationInfo = methodInfo.get(method);
        final Map<String, Object> parameters = methodEvaluationInfo.getParameters(args);
        //
        final String templateName = methodEvaluationInfo.getTemplateName();
        final String sql = queryManager.getQuery(templateName, parameters);
        final RowMapper<?> rowMapper = methodEvaluationInfo.getRowMapper();
        //
        return getResult(sql, rowMapper, methodEvaluationInfo.getReturnType());
    }

    // TODO: this should go to MethodEvaluationInfo
    private Object getResult(String sql, RowMapper<?> rowMapper, Class<?> returnType1) throws java.sql.SQLException {
        // TODO: improve type support
        final Object result;
        if(returnType1.isAssignableFrom(List.class)) {
            result = dataSourceAdapter.query(sql, rowMapper);
        } else {
            result = dataSourceAdapter.uniqueQuery(sql, rowMapper);
        }
        return result;
    }

}
