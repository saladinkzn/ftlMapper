package ru.shadam.ftlmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.ftlmapper.query.QueryInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * Actually, this is a proxy factory for "Repositories"
 *
 * @author Timur Shakurov
 */
public class RepositoryFactory {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);

    private QueryInvocationHandler queryInvocationHandler;

    public RepositoryFactory(QueryInvocationHandler queryInvocationHandler) {
        this.queryInvocationHandler = queryInvocationHandler;
    }

    public <T> T getMapper(Class<T> clazz) {
        if(!clazz.isInterface()) {
            throw new IllegalArgumentException("cannot create repository for class");
        }
        // TODO шки
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                queryInvocationHandler
        ));
    }

}
