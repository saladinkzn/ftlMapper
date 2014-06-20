package ru.shadam.ftlmapper.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for ResultSet -> DTO converters
 *
 * @author Timur Shakurov
 */
public interface RowMapper<T> {
    public T mapRow(ResultSet resultSet) throws SQLException;
}
