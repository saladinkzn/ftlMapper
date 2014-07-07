package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleShortColumnRowMapper extends SingleColumnRowMapper<Short> {
    public SingleShortColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    public SingleShortColumnRowMapper(boolean allowNull, int columnIndex) {
        super(allowNull, columnIndex);
    }

    public SingleShortColumnRowMapper(boolean allowNull, String columnName) {
        super(allowNull, columnName);
    }

    @Override
    protected Short getValue(ResultSetWrapper resultSet, int columnIndex) throws SQLException {
        return resultSet.getShort(columnIndex);
    }

    @Override
    protected Short getValue(ResultSetWrapper resultSet, String columnName) throws SQLException {
        return resultSet.getShort(columnName);
    }
}
