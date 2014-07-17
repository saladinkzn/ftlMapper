package ru.shadam.extractor.inner.simple.index;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class IntegerColumnIndexResultSetExtractor extends SimpleColumnIndexResultSetExtractor<Integer> {
    public IntegerColumnIndexResultSetExtractor(int columnIndex) {
        super(columnIndex);
    }

    @Override
    protected Integer extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getInt(columnIndex);
    }
}
