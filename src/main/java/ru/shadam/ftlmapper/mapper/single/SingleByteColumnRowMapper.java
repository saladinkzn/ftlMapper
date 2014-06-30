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

    @Override
    protected Byte getValue(ResultSetWrapper resultSet) throws SQLException {
        return resultSet.getByte(1);
    }
}
