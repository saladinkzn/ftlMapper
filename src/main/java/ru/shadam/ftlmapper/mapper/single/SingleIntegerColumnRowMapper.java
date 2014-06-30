package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleIntegerColumnRowMapper extends SingleColumnRowMapper<Integer> {

    public SingleIntegerColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    public SingleIntegerColumnRowMapper(boolean allowNull, int columnIndex) {
        super(allowNull, columnIndex);
    }

    public SingleIntegerColumnRowMapper(boolean allowNull, String columnName) {
        super(allowNull, columnName);
    }

    @Override
    protected Integer getValue(ResultSetWrapper resultSet, int columnIndex) throws SQLException {
        return resultSet.getInt(1);
    }

    @Override
    protected Integer getValue(ResultSetWrapper resultSet, String columnName) throws SQLException {
        return resultSet.getInt(columnName);
    }
}
