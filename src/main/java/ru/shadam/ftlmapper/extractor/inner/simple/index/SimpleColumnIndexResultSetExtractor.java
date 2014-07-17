package ru.shadam.ftlmapper.extractor.inner.simple.index;

import ru.shadam.ftlmapper.extractor.inner.simple.SimpleResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public abstract class SimpleColumnIndexResultSetExtractor<T> extends SimpleResultSetExtractor<T> {
    private final int columnIndex;

    public SimpleColumnIndexResultSetExtractor(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public T extractValue(ResultSet resultSet) throws SQLException {
        return extractValueColumnIndex(resultSet, columnIndex);
    }

    protected abstract T extractValueColumnIndex(ResultSet resultSet, int columnIndex) throws SQLException;

}
