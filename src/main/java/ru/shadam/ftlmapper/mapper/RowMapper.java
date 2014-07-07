package ru.shadam.ftlmapper.mapper;

import java.sql.SQLException;

/**
 * Interface for ResultSet -> DTO converters
 *
 * @author Timur Shakurov
 */
public interface RowMapper<T> {
    public T mapRow(ResultSetWrapper resultSet) throws SQLException;
}
