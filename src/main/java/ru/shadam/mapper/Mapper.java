package ru.shadam.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Actually, this is a proxy factory for "Repositories"
 *
 * @author Timur Shakurov
 */
public class Mapper {
    private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

    private DataSourceAdapter dataSourceAdapter;

    private QueryManager queryManager;

    public Mapper(DataSourceAdapter dataSourceAdapter, QueryManager queryManager) {
        this.dataSourceAdapter = dataSourceAdapter;
        this.queryManager = queryManager;
    }

    public <T> T getMapper(Class<T> clazz) {
        // TODO шки
        InvocationHandler mapperInvocationHandler = new MapperInvocationHandler(queryManager, dataSourceAdapter);
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                mapperInvocationHandler
        ));
    }

}
