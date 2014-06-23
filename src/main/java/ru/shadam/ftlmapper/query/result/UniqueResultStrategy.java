package ru.shadam.ftlmapper.query.result;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultStrategy;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class UniqueResultStrategy<T> implements ResultStrategy<T> {
    private final RowMapper<T> rowMapper;

    public UniqueResultStrategy(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T getResult(DataSourceAdapter dataSourceAdapter, String sql) throws SQLException {
        return dataSourceAdapter.uniqueQuery(sql, rowMapper);
    }
}
