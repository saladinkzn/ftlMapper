package ru.shadam.ftlmapper.mapper.annotation;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author sala
 */
public interface ResultSetConsumer<T> {
    public void consume(T instance, ResultSetWrapper resultSetRow) throws SQLException;
}
