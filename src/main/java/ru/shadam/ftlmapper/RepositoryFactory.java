package ru.shadam.ftlmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.ftlmapper.query.QueryInvocationHandler;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.reflect.Proxy;

/**
 * Actually, this is a proxy factory for "Repositories"
 *
 * @author Timur Shakurov
 */
public class RepositoryFactory {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);

    private QueryManager queryManager;
    private DataSourceAdapter dataSourceAdapter;

    public RepositoryFactory(QueryManager queryManager, DataSourceAdapter dataSourceAdapter) {
        this.queryManager = queryManager;
        this.dataSourceAdapter = dataSourceAdapter;
    }

    public <T> T getMapper(Class<T> clazz) {
        if(!clazz.isInterface()) {
            throw new IllegalArgumentException("cannot create repository for class");
        }
        // TODO шки
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new QueryInvocationHandler(clazz, queryManager, dataSourceAdapter)
        ));
    }

}
