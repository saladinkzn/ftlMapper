package ru.shadam.ftlmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.mapper.RowMapperFactory;
import ru.shadam.ftlmapper.mapper.single.SingleByteColumnRowMapper;
import ru.shadam.ftlmapper.mapper.single.SingleLongColumnRowMapper;
import ru.shadam.ftlmapper.mapper.single.SingleShortColumnRowMapper;
import ru.shadam.ftlmapper.mapper.single.SingleStringColumnRowMapper;
import ru.shadam.ftlmapper.query.QueryInvocationHandler;
import ru.shadam.ftlmapper.query.ResultSetExtractorFactory;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.Function;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.reflect.Proxy;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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
        final SingleByteColumnRowMapper singleByteColumnRowMapper = new SingleByteColumnRowMapper(false);
        final SingleByteColumnRowMapper singleByteColumnRowMapperNullable = new SingleByteColumnRowMapper(true);
        rowMapperFactory.register(byte.class, new Function<String, RowMapper<? extends Byte>>() {
            @Override
            public RowMapper<? extends Byte> get(String param) {
                if("".equals(param)) {
                    return singleByteColumnRowMapper;
                } else {
                    return new SingleByteColumnRowMapper(false, param);
                }
            }
        });
        rowMapperFactory.register(Byte.class, new Function<String, RowMapper<? extends Byte>>() {
            @Override
            public RowMapper<? extends Byte> get(String param) {
                if("".equals(param)) {
                    return singleByteColumnRowMapperNullable;
                }
                return new SingleByteColumnRowMapper(true, param);
            }
        });
        // Short
        final SingleShortColumnRowMapper singleShortColumnRowMapper = new SingleShortColumnRowMapper(false);
        final SingleShortColumnRowMapper singleShortColumnRowMapperNullable = new SingleShortColumnRowMapper(true);
        rowMapperFactory.register(short.class, new Function<String, RowMapper<? extends Short>>() {
            @Override
            public RowMapper<? extends Short> get(String param) {
                if("".equals(param)) {
                    return singleShortColumnRowMapper;
                } else {
                    return new SingleShortColumnRowMapper(false, param);
                }
            }
        });
        rowMapperFactory.register(Short.class, new Function<String, RowMapper<? extends Short>>() {
            @Override
            public RowMapper<? extends Short> get(String param) {
                if("".equals(param)) {
                    return singleShortColumnRowMapperNullable;
                } else {
                    return new SingleShortColumnRowMapper(true, param);
                }
            }
        });
        // Long
        final SingleLongColumnRowMapper singleLongColumnRowMapper = new SingleLongColumnRowMapper(false);
        final SingleLongColumnRowMapper singleLongColumnRowMapperNullable = new SingleLongColumnRowMapper(true);
        rowMapperFactory.register(long.class, new Function<String, RowMapper<? extends Long>>() {
            @Override
            public RowMapper<? extends Long> get(String param) {
                if("".equals(param)) {
                    return singleLongColumnRowMapper;
                } else {
                    return new SingleLongColumnRowMapper(false, param);
                }
            }
        });
        rowMapperFactory.register(Long.class, new Function<String, RowMapper<? extends Long>>() {
            @Override
            public RowMapper<? extends Long> get(String param) {
                if("".equals(param)) {
                    return singleLongColumnRowMapperNullable;
                } else {
                    return new SingleLongColumnRowMapper(true, param);
                }
            }
        });
        // String
        final SingleStringColumnRowMapper singleStringColumnRowMapper = new SingleStringColumnRowMapper(1);
        rowMapperFactory.register(String.class, new Function<String, RowMapper<? extends String>>() {
            @Override
            public RowMapper<? extends String> get(String param) {
                if("".equals(param)) {
                    return singleStringColumnRowMapper;
                }
                return new SingleStringColumnRowMapper(param);
            }
        });
        final RowMapper<Object[]> objectArrayRowMapper = new RowMapper<Object[]>() {
            @Override
            public Object[] mapRow(ResultSetWrapper resultSet) throws SQLException {
                final ResultSetMetaData metaData = resultSet.getMetaData();
                final int columnCount = metaData.getColumnCount();
                Object[] result = new Object[columnCount];
                for(int i = 1; i <= columnCount; i++) {
                    result[i-1] = resultSet.getObject(i);
                }
                return result;
            }
        };
        rowMapperFactory.register(Object[].class, new Function<String, RowMapper<? extends Object[]>>() {
            @Override
            public RowMapper<? extends Object[]> get(String param) {
                return objectArrayRowMapper;
            }
        });
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
