package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleByteColumnRowMapper extends SingleColumnRowMapper<Byte> {
    public SingleByteColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    public SingleByteColumnRowMapper(boolean allowNull, int columnIndex) {
        super(allowNull, columnIndex);
    }

    public SingleByteColumnRowMapper(boolean allowNull, String columnName) {
        super(allowNull, columnName);
    }

    @Override
    protected Byte getValue(ResultSetWrapper resultSet, int columnIndex) throws SQLException {
        return resultSet.getByte(columnIndex);
    }

    @Override
    protected Byte getValue(ResultSetWrapper resultSet, String columnName) throws SQLException {
        return resultSet.getByte(columnName);
    }
}
