package ru.shadam.ftlmapper.query.result;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultStrategy;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * @author Timur Shakurov
 */
public class CollectionResultStrategy<T> implements ResultStrategy<T> {
    public static boolean isSupported(Class<?> clazz) {
        return (clazz.equals(List.class) || clazz.equals(Set.class));
    }

    private RowMapper<T> rowMapper;
    private Class<?> targetClass;

    public CollectionResultStrategy(Class<?> targetClass, RowMapper<T> rowMapper) {
        if(!isSupported(targetClass)) {
            throw new IllegalArgumentException("class: " + targetClass + " is not supported");
        }
        this.targetClass = targetClass;
        this.rowMapper = rowMapper;
    }

    @Override
    public Object getResult(DataSourceAdapter dataSourceAdapter, String sql) throws SQLException {
        if(targetClass.equals(List.class)) {
            return dataSourceAdapter.query(sql, rowMapper);
        } else if (targetClass.equals(Set.class)) {
            return dataSourceAdapter.queryForSet(sql, rowMapper);
        } else {
            throw new IllegalStateException("targetClass is not supported");
        }
    }
}
