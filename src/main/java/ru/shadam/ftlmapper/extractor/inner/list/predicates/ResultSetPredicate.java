package ru.shadam.ftlmapper.extractor.inner.list.predicates;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public interface ResultSetPredicate<T extends PredicateState> {
    public T newState(ResultSet resultSet) throws SQLException;

    public boolean apply(T state, ResultSet resultSet) throws SQLException;
}
