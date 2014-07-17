package ru.shadam.extractor.inner.simple;

import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public abstract class SimpleResultSetExtractor<T> implements InnerResultSetExtractor<SimpleState<T>> {
    @Override
    public SimpleState<T> consumeRow(SimpleState<T> simpleState, ResultSet resultSet) throws SQLException {
        return new SimpleState<>(true, extractValue(resultSet));
    }

    @Override
    public SimpleState<T> complete(SimpleState<T> simpleState) {
        return simpleState;
    }

    @Override
    public SimpleState<T> getNewState(SimpleState<T> oldState) {
        return new SimpleState<>();
    }

    public abstract T extractValue(ResultSet resultSet) throws SQLException;
}
