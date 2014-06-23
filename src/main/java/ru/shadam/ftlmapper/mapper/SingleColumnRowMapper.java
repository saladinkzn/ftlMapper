package ru.shadam.ftlmapper.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleColumnRowMapper<T> implements RowMapper<T> {
    private final Class<T> clazz;

    public SingleColumnRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        return clazz.cast(resultSet.getObject(1));
    }
}
