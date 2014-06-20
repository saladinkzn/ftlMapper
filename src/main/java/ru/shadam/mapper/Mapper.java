package ru.shadam.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * Actually, this is a proxy factory for "Repositories"
 *
 * @author Timur Shakurov
 */
public class Mapper {
    private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

    private QueryInvocationHandler queryInvocationHandler;

    public Mapper(QueryInvocationHandler queryInvocationHandler) {
        this.queryInvocationHandler = queryInvocationHandler;
    }

    public <T> T getMapper(Class<T> clazz) {
        // TODO шки
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                queryInvocationHandler
        ));
    }

}
