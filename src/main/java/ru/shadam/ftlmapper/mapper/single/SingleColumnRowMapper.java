package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public abstract class SingleColumnRowMapper<T> implements RowMapper<T> {
    private final MapperType mapperType;
    private final boolean allowNull;

    private int columnIndex = -1;
    private String columnName = null;

    protected SingleColumnRowMapper(boolean allowNull) {
        this(allowNull, 1);
    }

    protected SingleColumnRowMapper(boolean allowNull, int columnIndex) {
        this.allowNull = allowNull;
        this.columnIndex = columnIndex;
        this.mapperType = MapperType.INDEX;
    }

    protected SingleColumnRowMapper(boolean allowNull, String columnName) {
        this.allowNull = allowNull;
        this.columnName = columnName;
        this.mapperType = MapperType.NAME;
    }

    @Override
    public T mapRow(ResultSetWrapper resultSet) throws SQLException {
        final T value;
        switch (mapperType) {
            case INDEX:
                value = getValue(resultSet, columnIndex);
                break;
            case NAME:
                value = getValue(resultSet, columnName);
                break;
            default:
                throw new IllegalStateException("Unsupported mapperType: " + mapperType);
        }
        return allowNull && resultSet.wasNull() ? null : value;
    }

    protected abstract T getValue(ResultSetWrapper resultSet, int columnIndex) throws SQLException;

    protected abstract T getValue(ResultSetWrapper resultSet, String columnName) throws SQLException;
}
