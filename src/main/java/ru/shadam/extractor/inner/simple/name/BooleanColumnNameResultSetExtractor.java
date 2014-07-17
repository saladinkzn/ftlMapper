package ru.shadam.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class BooleanColumnNameResultSetExtractor extends SimpleColumnNameResultSetExtractor<Boolean> {
    public BooleanColumnNameResultSetExtractor(String columnName) {
        super(columnName);
    }

    @Override
    protected Boolean extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getBoolean(columnName);
    }
}
