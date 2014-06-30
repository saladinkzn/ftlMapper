package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleLongColumnRowMapper extends SingleColumnRowMapper<Long> {
    public SingleLongColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    public SingleLongColumnRowMapper(boolean allowNull, String columnName) {
        super(allowNull, columnName);
    }

    public SingleLongColumnRowMapper(boolean allowNull, int columnIndex) {
        super(allowNull, columnIndex);
    }

    @Override
    protected Long getValue(ResultSetWrapper resultSet, int columnIndex) throws SQLException {
        return resultSet.getLong(columnIndex);
    }

    @Override
    protected Long getValue(ResultSetWrapper resultSet, String columnName) throws SQLException {
        return resultSet.getLong(columnName);
    }
}
