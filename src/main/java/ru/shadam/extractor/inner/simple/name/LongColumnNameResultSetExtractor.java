package ru.shadam.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class LongColumnNameResultSetExtractor extends SimpleColumnNameResultSetExtractor<Long> {
    public LongColumnNameResultSetExtractor(String columnName) {
        super(columnName);
    }

    @Override
    protected Long extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getLong(columnName);
    }
}
