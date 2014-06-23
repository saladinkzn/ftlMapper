package ru.shadam.ftlmapper.mapper.single;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleIntegerColumnRowMapper extends SingleColumnRowMapper<Integer> {

    public SingleIntegerColumnRowMapper(boolean allowNull) {
        super(allowNull);
    }

    @Override
    protected Integer getValue(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1);
    }
}
