package ru.shadam.ftlmapper.mapper.annotation;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author sala
 */
public interface NewInstanceSupplier<T> {
    public T newInstance(ResultSetWrapper resultSetWrapper) throws SQLException;
}
