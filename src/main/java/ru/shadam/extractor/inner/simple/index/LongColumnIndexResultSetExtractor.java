package ru.shadam.extractor.inner.simple.index;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class LongColumnIndexResultSetExtractor extends SimpleColumnIndexResultSetExtractor<Long> {
    public LongColumnIndexResultSetExtractor(int columnIndex) {
        super(columnIndex);
    }

    @Override
    protected Long extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getLong(columnIndex);
    }
}
