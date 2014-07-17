package ru.shadam.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class IntegerColumnNameResultSetExtractor extends SimpleColumnNameResultSetExtractor<Integer> {
    public IntegerColumnNameResultSetExtractor(String columnName) {
        super(columnName);
    }

    @Override
    protected Integer extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getInt(columnName);
    }
}
