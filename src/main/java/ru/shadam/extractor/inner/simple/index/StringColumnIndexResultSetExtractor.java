package ru.shadam.extractor.inner.simple.index;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class StringColumnIndexResultSetExtractor extends SimpleColumnIndexResultSetExtractor<String> {
    public StringColumnIndexResultSetExtractor(int columnIndex) {
        super(columnIndex);
    }

    @Override
    protected String extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getString(columnIndex);
    }
}
