package ru.shadam.extractor.inner.list.predicates;

import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author sala
 */
public class CheckAttributesPredicate implements ResultSetPredicate<ValuesPredicateState> {
    private final List<InnerResultSetExtractor<? extends SimpleState<?>>> extractors;

    public CheckAttributesPredicate(List<InnerResultSetExtractor<? extends SimpleState<?>>> extractors) {
        this.extractors = extractors;
    }

    private List<Object> extractValues(ResultSet resultSet) throws SQLException {
        List<Object> rsValues = new ArrayList<>();
        for(InnerResultSetExtractor<? extends SimpleState<?>> extractor: extractors) {
            final SimpleState<?> simpleState = extractor.getNewState(null);
            final SimpleState<?> consumed = ((InnerResultSetExtractor<SimpleState<?>>)extractor).consumeRow(simpleState, resultSet);
            if(!consumed.isCompleted()) {
                throw new IllegalStateException("Extractor should be attribute-like");
            }
            rsValues.add(consumed.getValue());
        }
        return rsValues;
    }

    @Override
    public ValuesPredicateState newState(ResultSet resultSet) throws SQLException {
        return new ValuesPredicateState(extractValues(resultSet));
    }

    @Override
    public boolean apply(ValuesPredicateState state, ResultSet resultSet) throws SQLException {
        final List<Object> values = state.getValues();
        final List<Object> rsValues = extractValues(resultSet);
        //
        for(int i = 0; i < values.size(); i++) {
            if(!Objects.equals(values.get(i), rsValues.get(i))) {
                return true;
            }
        }
        return false;
    }
}
