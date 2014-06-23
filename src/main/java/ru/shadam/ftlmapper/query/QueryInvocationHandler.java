package ru.shadam.ftlmapper.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * InvocationHandler for our "Repository"
 *
 * @author Timur Shakurov
 */
public class QueryInvocationHandler implements InvocationHandler {
    private final static Logger logger = LoggerFactory.getLogger(QueryInvocationHandler.class);

    private DataSourceAdapter dataSourceAdapter;

    private Map<Method, MethodEvaluationInfo> methodInfo;

    public QueryInvocationHandler(Class<?> targetClass, QueryManager queryManager, DataSourceAdapter dataSourceAdapter) {
        this.dataSourceAdapter = dataSourceAdapter;
        //
        methodInfo = new HashMap<>();
        final Method[] methods = targetClass.getDeclaredMethods();
        for(Method method: methods) {
            try {
                methodInfo.put(method, new MethodEvaluationInfo(queryManager, method));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
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
        final String sql = methodEvaluationInfo.getQueryStrategy().getSql(parameters);
        //
        return methodEvaluationInfo.getResultStrategy().getResult(dataSourceAdapter, sql);
    }

}
