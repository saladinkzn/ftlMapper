package ru.shadam.ftlmapper.mapper.annotation;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sala
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {
    private final NewInstanceSupplier<T> newInstanceSupplier;
    private final List<ResultSetConsumer<T>> resultSetConsumers;

    public AnnotationRowMapper(NewInstanceSupplier<T> newInstanceSupplier, List<ResultSetConsumer<T>> resultSetConsumers) {
        this.newInstanceSupplier = newInstanceSupplier;
        this.resultSetConsumers = resultSetConsumers;
    }

    @Override
    public T mapRow(ResultSetWrapper resultSet) throws SQLException {
        final T instance = newInstanceSupplier.newInstance(resultSet);
        for(ResultSetConsumer<T> resultSetConsumer: resultSetConsumers) {
            resultSetConsumer.consume(instance, resultSet);
        }
        return instance;
    }

}
