package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.CollectionState;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 */
public abstract class CollectionResultSetExtractor<T, U extends Collection<T>> implements InnerResultSetExtractor<CollectionState<T, U>> {
    private final InnerResultSetExtractor<SimpleState<T>> innerResultSetExtractor;
    private final ResultSetPredicate<PredicateState> listCompletePredicate;
    //

    public CollectionResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor,
                                  ResultSetPredicate<? extends PredicateState> listCompletePredicate) {
        this.innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<T>>) innerResultSetExtractor;
        this.listCompletePredicate = (ResultSetPredicate<PredicateState>) listCompletePredicate;

    }

    @Override
    public CollectionState<T, U> getNewState(CollectionState<T, U> oldState) {
        if(oldState == null) {
            return new CollectionState<>(newValue(), innerResultSetExtractor.getNewState(null), null);
        } else {
            return new CollectionState<>(oldState.getTempValue(), innerResultSetExtractor.getNewState(oldState.getChildState()), null);
        }
    }

    @Override
    public CollectionState<T, U> consumeRow(CollectionState<T, U> listState, ResultSet resultSet) throws SQLException {
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
        if(newestChildState.isCompleted()) {
            tempValue.add(newestChildState.getValue());
        }
        return new CollectionState<>(completed, value, tempValue, newestChildState, newPredicateState);
    }

    @Override
    public CollectionState<T, U> complete(CollectionState<T, U> state) {
        if (state.isCompleted()) {
            return state;
        }
        final U newTempValue = newValue(state.getTempValue());
        final SimpleState<T> simpleState;
        if(!state.getChildState().isCompleted()) {
            simpleState = innerResultSetExtractor.complete(state.getChildState());
            newTempValue.add(simpleState.getValue());
        } else {
            simpleState = state.getChildState();
        }
        final U value = newValue(newTempValue);
        return new CollectionState<>(true, value, newValue(), simpleState, state.getPredicateState());
    }

    public abstract U newValue();

    public abstract U newValue(U oldValue);
}
