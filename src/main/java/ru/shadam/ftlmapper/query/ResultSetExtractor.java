package ru.shadam.ftlmapper.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public interface ResultSetExtractor<T> {
    public T extractResult(ResultSet resultSet) throws SQLException;
}
