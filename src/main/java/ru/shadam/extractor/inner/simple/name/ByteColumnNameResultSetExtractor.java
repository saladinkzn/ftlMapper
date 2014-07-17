package ru.shadam.extractor.inner.simple.name;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class ByteColumnNameResultSetExtractor extends SimpleColumnNameResultSetExtractor<Byte> {

    public ByteColumnNameResultSetExtractor(String columnName) {
        super(columnName);
    }

    @Override
    protected Byte extractValueColumnName(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getByte(columnName);
    }
}
