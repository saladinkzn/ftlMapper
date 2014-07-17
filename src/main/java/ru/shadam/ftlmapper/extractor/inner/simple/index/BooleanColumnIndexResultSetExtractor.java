package ru.shadam.ftlmapper.extractor.inner.simple.index;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class BooleanColumnIndexResultSetExtractor extends SimpleColumnIndexResultSetExtractor<Boolean> {
    public BooleanColumnIndexResultSetExtractor(int columnIndex) {
        super(columnIndex);
    }

    @Override
    protected Boolean extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getBoolean(columnIndex);
    }
}
