package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public abstract class SingleColumnRowMapper<T> implements RowMapper<T> {
    private final boolean allowNull;

    protected SingleColumnRowMapper(boolean allowNull) {
        this.allowNull = allowNull;
    }

    @Override
    public T mapRow(ResultSetWrapper resultSet) throws SQLException {
        final T value = getValue(resultSet);
        return allowNull && resultSet.wasNull() ? null : value;
    }

    protected abstract T getValue(ResultSetWrapper resultSet) throws SQLException;
}
