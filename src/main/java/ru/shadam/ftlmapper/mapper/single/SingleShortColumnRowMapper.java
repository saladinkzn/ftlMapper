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

    @Override
    protected Short getValue(ResultSetWrapper resultSet) throws SQLException {
        return resultSet.getShort(1);
    }
}
