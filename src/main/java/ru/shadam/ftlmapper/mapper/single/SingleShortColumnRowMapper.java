package ru.shadam.ftlmapper.mapper.single;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleShortColumnRowMapper extends SingleColumnRowMapper {
    public SingleShortColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    @Override
    protected Object getValue(ResultSet resultSet) throws SQLException {
        return resultSet.getShort(1);
    }
}
