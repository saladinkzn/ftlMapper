package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class UniqueResultStrategy implements ResultStrategy {
    @Override
    public <T> Object getResult(DataSourceAdapter dataSourceAdapter, String sql, RowMapper<T> rowMapper) throws SQLException {
        return dataSourceAdapter.uniqueQuery(sql, rowMapper);
    }
}
