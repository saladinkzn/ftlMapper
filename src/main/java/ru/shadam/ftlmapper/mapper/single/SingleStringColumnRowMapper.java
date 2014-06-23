package ru.shadam.ftlmapper.mapper.single;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleStringColumnRowMapper implements ru.shadam.ftlmapper.mapper.RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet) throws SQLException {
        return resultSet.getString(1);
    }
}
