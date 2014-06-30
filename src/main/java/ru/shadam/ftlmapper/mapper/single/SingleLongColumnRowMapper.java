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

    @Override
    protected Long getValue(ResultSetWrapper resultSet) throws SQLException {
        return resultSet.getLong(1);
    }
}
