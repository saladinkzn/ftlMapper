package ru.shadam.ftlmapper.extractor.inner.simple.index;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class ShortColumnIndexResultSetExtractor extends SimpleColumnIndexResultSetExtractor<Short> {
    public ShortColumnIndexResultSetExtractor(int columnIndex) {
        super(columnIndex);
    }

    @Override
    protected Short extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getShort(columnIndex);
    }
}
