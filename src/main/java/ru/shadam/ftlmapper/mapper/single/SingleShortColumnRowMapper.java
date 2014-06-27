package ru.shadam.ftlmapper.mapper.single;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleShortColumnRowMapper extends SingleColumnRowMapper<Short> {
    public SingleShortColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    @Override
    protected Short getValue(ResultSet resultSet) throws SQLException {
        return resultSet.getShort(1);
    }
}
