package ru.shadam.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class RootResultSetExtractor<T> implements ResultSetExtractor<T> {
    private static final Logger logger = LoggerFactory.getLogger(RootResultSetExtractor.class);

    private final InnerResultSetExtractor<SimpleState<T>> innerResultSetExtractor;

    public RootResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor) {
        this.innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<T>>)innerResultSetExtractor;
    }

    @Override
    public T extractResult(ResultSet resultSet) throws SQLException {
        logger.debug("Result extracting has begun");
        //
        SimpleState<T> simpleState = innerResultSetExtractor.getNewState(null);// TODO;
        //
        logger.trace("{}", simpleState);
        //
        while (resultSet.next()) {
            logger.debug("Consuming row");
            simpleState = innerResultSetExtractor.consumeRow(simpleState, resultSet);
            logger.trace("{}", simpleState);
            if(simpleState.isCompleted()) {
                logger.debug("Extractor was completed");
                //
                final T value = simpleState.getValue();
                //
                logger.debug("Result extracting has finished: {}", value);
                //
                return value;
            }
        }
        //
        logger.debug("Result set was completed");
        //
        final SimpleState<T> completedState = innerResultSetExtractor.complete(simpleState);
        //
        logger.trace("{}", completedState);
        //
        final T value = completedState.getValue();
        //
        logger.debug("Result extracting has finished: {}", value);
        //
        return value;
    }
}
