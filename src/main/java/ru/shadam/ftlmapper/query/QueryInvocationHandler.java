package ru.shadam.ftlmapper.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.ast.ASTParser;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.PreparedStatementCreator;
import ru.shadam.ftlmapper.util.QueryManager;
import ru.shadam.extractor.ResultSetExtractorFactory;

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

    public QueryInvocationHandler(Class<?> targetClass, QueryManager queryManager, DataSourceAdapter dataSourceAdapter, ResultSetExtractorFactory rowMapperFactory, ASTParser astParser) {
        this.dataSourceAdapter = dataSourceAdapter;
        //
        methodInfo = new HashMap<>();
        final Method[] methods = targetClass.getDeclaredMethods();
        for(Method method: methods) {
            try {
                methodInfo.put(method, new MethodEvaluationInfo(queryManager, method, rowMapperFactory, astParser));
            } catch (Exception ex) {
                logger.error("An exception has occurred while gathering method evaluation information for method: " + method.getName());
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
        //
        final PreparedStatementCreator psc = methodEvaluationInfo.getQueryStrategy().getSql(args);
        //
        return dataSourceAdapter.query(psc, methodEvaluationInfo.getResultSetExtractor());
    }

}
