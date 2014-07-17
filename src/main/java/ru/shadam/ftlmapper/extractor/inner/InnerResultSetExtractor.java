package ru.shadam.ftlmapper.extractor.inner;

import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface of single row processor
 *
 * @author sala
 */
public interface InnerResultSetExtractor<T extends SimpleState<?>> {

    /**
     * Returns new state. Provides old state (e.g. for ListState)
     *
     * @param oldState previous state (for extractors which stores state). null if
     * @return
     */
    public T getNewState(T oldState);

    /**
     * Consumes row to update a state
     *
     * @param state old state
     * @param resultSet result set to extract values
     * @return new state
     * @throws java.sql.SQLException
     */
    public T consumeRow(T state, ResultSet resultSet) throws SQLException;

    /**
     * Notifies extractor that result set finished
     *
     * @param state last state
     * @return finalized state
     */
    public T complete(T state);
}
