package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.ChildPredicateState;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public abstract class CollectionResultSetExtractor<T, U, V extends ChildPredicateState<T, U>> implements InnerResultSetExtractor<V> {
    private final InnerResultSetExtractor<SimpleState<T>> innerResultSetExtractor;
    private final ResultSetPredicate<PredicateState> listCompletePredicate;
    //

    public CollectionResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor,
                                  ResultSetPredicate<? extends PredicateState> listCompletePredicate) {
        this.innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<T>>) innerResultSetExtractor;
        this.listCompletePredicate = (ResultSetPredicate<PredicateState>) listCompletePredicate;

    }

    @Override
    public V getNewState(V oldState) {
        if(oldState == null) {
            return newState(newValue(), innerResultSetExtractor.getNewState(null), null);
        } else {
            return newState(oldState.getTempValue(), innerResultSetExtractor.getNewState(oldState.getChildState()), null);
        }
    }

    @Override
    public V consumeRow(V listState, ResultSet resultSet) throws SQLException {
        final SimpleState<T> childState;
        if(listState.getChildState().isCompleted()) {
            childState = innerResultSetExtractor.getNewState(listState.getChildState());
        } else {
            childState = listState.getChildState();
        }
        // TODO:
        final PredicateState predicateState;
        if(listState.getPredicateState() == null) {
            predicateState = listCompletePredicate.newState(resultSet);
        } else {
            predicateState = listState.getPredicateState();
        }
        //
        final SimpleState<T> newestChildState = innerResultSetExtractor.consumeRow(childState, resultSet);
        //
        final boolean completed = listCompletePredicate.apply(predicateState, resultSet);
        // this is calculate() method
        final U value;
        final U tempValue;
        final PredicateState newPredicateState;
        if(completed) {
            value = newValue(listState.getTempValue());
            tempValue = newValue();
            newPredicateState = listCompletePredicate.newState(resultSet);
        } else {
            value = listState.getValue();
            tempValue = newValue(listState.getTempValue());
            newPredicateState = predicateState;
        }
        //
        final U newTempValue;
        if(newestChildState.isCompleted()) {
            final T childValue = newestChildState.getValue();
            newTempValue = addNewValue(tempValue, childValue);
        } else {
            newTempValue = tempValue;
        }
        return newState(completed, value, newTempValue, newestChildState, newPredicateState);
    }

    @Override
    public V complete(V state) {
        if (state.isCompleted()) {
            return state;
        }
        final U newTempValue = newValue(state.getTempValue());
        final SimpleState<T> simpleState;
        final U newestTempValue;
        if(!state.getChildState().isCompleted()) {
            simpleState = innerResultSetExtractor.complete(state.getChildState());
            final T childValue = simpleState.getValue();
            newestTempValue = addNewValue(newTempValue, childValue);
        } else {
            simpleState = state.getChildState();
            newestTempValue = newTempValue;
        }
        final U value = newValue(newestTempValue);
        return newState(true, value, newValue(), simpleState, state.getPredicateState());
    }

    protected abstract U newValue();

    protected abstract U newValue(U oldValue);

    protected abstract U addNewValue(U tempValue, T childValue);

    protected abstract V newState(U newValue, SimpleState<T> state, PredicateState predicateState);

    protected abstract V newState(boolean completed, U value, U newValue, SimpleState<T> state, PredicateState predicateState);
}
