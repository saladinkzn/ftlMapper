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

    public Mapper(DataSourceAdapter dataSourceAdapter) {
        this.dataSourceAdapter = dataSourceAdapter;
    }

    public <T> T getMapper(Class<T> clazz) {
        // TODO шки
        InvocationHandler mapperInvocationHandler = new MapperInvocationHandler(dataSourceAdapter);
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                mapperInvocationHandler
        ));
    }

}
