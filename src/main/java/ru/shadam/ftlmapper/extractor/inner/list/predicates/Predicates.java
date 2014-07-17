package ru.shadam.ftlmapper.extractor.inner.list.predicates;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public abstract class Predicates {
    private static final ResultSetPredicate<PredicateState> ALWAYS_FALSE_PREDICATE = new ResultSetPredicate<PredicateState>() {
        @Override
        public PredicateState newState(ResultSet resultSet) throws SQLException {
            return new PredicateState();
        }

        @Override
        public boolean apply(PredicateState state, ResultSet resultSet) throws SQLException {
            return false;
        }

    };

    public static ResultSetPredicate<PredicateState> alwaysFalse() {
        return ALWAYS_FALSE_PREDICATE;
    }
}
