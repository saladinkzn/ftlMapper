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

    @Override
    protected Integer getValue(ResultSetWrapper resultSet) throws SQLException {
        return resultSet.getInt(1);
    }
}
