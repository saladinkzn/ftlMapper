package ru.shadam.extractor.inner.simple.index;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class ByteColumnIndexResultSetExtractor extends SimpleColumnIndexResultSetExtractor<Byte> {
    public ByteColumnIndexResultSetExtractor(int columnIndex) {
        super(columnIndex);
    }

    @Override
    protected Byte extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getByte(columnIndex);
    }
}
