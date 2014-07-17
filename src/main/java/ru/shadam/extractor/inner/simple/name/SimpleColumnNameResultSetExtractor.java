package ru.shadam.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public abstract class SimpleColumnNameResultSetExtractor<T> extends ru.shadam.extractor.inner.simple.SimpleResultSetExtractor<T> {
    private final String columnName;
    //

    public SimpleColumnNameResultSetExtractor(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public T extractValue(ResultSet resultSet) throws SQLException {
        return extractValueColumnName(resultSet, columnName);
    }

    protected abstract T extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException;
}
