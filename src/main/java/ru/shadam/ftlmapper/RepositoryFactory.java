package ru.shadam.ftlmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.ftlmapper.mapper.RowMapperFactory;
import ru.shadam.ftlmapper.mapper.single.SingleByteColumnRowMapper;
import ru.shadam.ftlmapper.mapper.single.SingleLongColumnRowMapper;
import ru.shadam.ftlmapper.mapper.single.SingleShortColumnRowMapper;
import ru.shadam.ftlmapper.mapper.single.SingleStringColumnRowMapper;
import ru.shadam.ftlmapper.query.QueryInvocationHandler;
import ru.shadam.ftlmapper.query.ResultSetExtractorFactory;
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
    private ResultSetExtractorFactory resultSetExtractorFactory;

    {
        // Default row mapper factory.
        final RowMapperFactory rowMapperFactory = new RowMapperFactory();
        // Byte
        rowMapperFactory.register(byte.class, new SingleByteColumnRowMapper(false));
        rowMapperFactory.register(Byte.class, new SingleByteColumnRowMapper(true));
        // Short
        rowMapperFactory.register(short.class, new SingleShortColumnRowMapper(false));
        rowMapperFactory.register(Short.class, new SingleShortColumnRowMapper(true));
        // Long
        rowMapperFactory.register(long.class, new SingleLongColumnRowMapper(false));
        rowMapperFactory.register(Long.class, new SingleLongColumnRowMapper(true));
        // String
        rowMapperFactory.register(String.class, new SingleStringColumnRowMapper());
        //
        resultSetExtractorFactory = new ResultSetExtractorFactory(rowMapperFactory);
    }


    public RepositoryFactory(QueryManager queryManager, DataSourceAdapter dataSourceAdapter) {
        this.queryManager = queryManager;
        this.dataSourceAdapter = dataSourceAdapter;
    }

    public void setRowMapperFactory(ResultSetExtractorFactory resultSetExtractorFactory) {
        this.resultSetExtractorFactory = resultSetExtractorFactory;
    }

    public <T> T getMapper(Class<T> clazz) {
        if(!clazz.isInterface()) {
            throw new IllegalArgumentException("cannot create repository for class");
        }
        // TODO шки
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new QueryInvocationHandler(clazz, queryManager, dataSourceAdapter, resultSetExtractorFactory)
        ));
    }

}
