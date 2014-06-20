package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public interface ResultStrategy {
    <T> Object getResult(DataSourceAdapter dataSourceAdapter, String sql, RowMapper<T> rowMapper) throws SQLException;
}
