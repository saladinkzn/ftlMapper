package ru.shadam.ftlmapper.query.result;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultStrategy;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ListResultStrategy implements ResultStrategy {
    @Override
    public <T> List<T> getResult(DataSourceAdapter dataSourceAdapter, String sql, RowMapper<T> rowMapper) throws SQLException {
        return dataSourceAdapter.query(sql, rowMapper);
    }
}
