package ru.shadam.ftlmapper.query.result;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultStrategy;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class UniqueResultStrategy implements ResultStrategy {
    @Override
    public <T> T getResult(DataSourceAdapter dataSourceAdapter, String sql, RowMapper<T> rowMapper) throws SQLException {
        return dataSourceAdapter.uniqueQuery(sql, rowMapper);
    }
}
