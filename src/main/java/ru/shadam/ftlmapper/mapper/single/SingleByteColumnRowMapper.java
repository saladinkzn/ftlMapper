package ru.shadam.ftlmapper.mapper.single;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleByteColumnRowMapper extends SingleColumnRowMapper<Byte> {
    public SingleByteColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    @Override
    protected Byte getValue(ResultSet resultSet) throws SQLException {
        return resultSet.getByte(1);
    }
}
