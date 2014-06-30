package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleStringColumnRowMapper implements ru.shadam.ftlmapper.mapper.RowMapper<String> {
    @Override
    public String mapRow(ResultSetWrapper resultSet) throws SQLException {
        return resultSet.getString(1);
    }
}
