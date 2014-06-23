package ru.shadam.ftlmapper.query.result;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultStrategy;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ListResultStrategy<T> implements ResultStrategy<T> {
    private final RowMapper<T> rowMapper;

    public ListResultStrategy(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> getResult(DataSourceAdapter dataSourceAdapter, String sql) throws SQLException {
        return dataSourceAdapter.query(sql, rowMapper);
    }
}
