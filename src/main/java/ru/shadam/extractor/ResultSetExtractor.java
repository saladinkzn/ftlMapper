package ru.shadam.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface ResultSetExtractor<T> {
    /**
     * Extracts value from result set.
     * @param resultSet result set
     * @return Value
     */
    T extractResult(ResultSet resultSet) throws SQLException;
}
