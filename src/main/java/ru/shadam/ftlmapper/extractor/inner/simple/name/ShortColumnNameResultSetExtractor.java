package ru.shadam.ftlmapper.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class ShortColumnNameResultSetExtractor extends SimpleColumnNameResultSetExtractor<Short> {
    public ShortColumnNameResultSetExtractor(String columnName) {
        super(columnName);
    }

    @Override
    protected Short extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getShort(columnName);
    }
}
