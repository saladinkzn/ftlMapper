package ru.shadam.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A No-op mapper used to map
 *
 * @author Timur Shakurov
 */
public class NullRowMapper implements RowMapper<Object> {
    @Override
    public Object mapRow(ResultSet resultSet) throws SQLException {
        return null;
    }
}
