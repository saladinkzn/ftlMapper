package ru.shadam.ftlmapper.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class StringColumnNameResultSetExtractor extends SimpleColumnNameResultSetExtractor<String> {
    public StringColumnNameResultSetExtractor(String columnName) {
        super(columnName);
    }

    @Override
    protected String extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getString(columnName);
    }
}
