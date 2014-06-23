package ru.shadam.ftlmapper.mapper.single;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleLongColumnRowMapper extends SingleColumnRowMapper<Long> {
    public SingleLongColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    @Override
    protected Long getValue(ResultSet resultSet) throws SQLException {
        return resultSet.getLong(1);
    }
}
