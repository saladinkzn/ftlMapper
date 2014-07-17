package ru.shadam.extractor.inner.list;

import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.inner.list.predicates.PredicateState;
import ru.shadam.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.extractor.state.ListState;
import ru.shadam.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sala
 */
public class ListResultSetExtractor<T> implements InnerResultSetExtractor<ListState<T>> {
    private final InnerResultSetExtractor<SimpleState<T>> innerResultSetExtractor;
    private final ResultSetPredicate<PredicateState> listCompletePredicate;
    //

    public ListResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor,
                                  ResultSetPredicate<? extends PredicateState> listCompletePredicate) {
        this.innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<T>>) innerResultSetExtractor;
        this.listCompletePredicate = (ResultSetPredicate<PredicateState>) listCompletePredicate;

    }

    @Override
    public ListState<T> complete(ListState<T> state) {
        if (state.isCompleted()) {
            return state;
        }
        final List<T> newTempValue = new ArrayList<>(state.getTempValue());
        final SimpleState<T> simpleState;
        if(!state.getChildState().isCompleted()) {
            simpleState = innerResultSetExtractor.complete(state.getChildState());
            newTempValue.add(simpleState.getValue());
        } else {
            simpleState = state.getChildState();
        }
        final List<T> value = calculateValue(newTempValue);
        return new ListState<>(true, value, new ArrayList<T>(), simpleState, state.getPredicateState());
    }

    private List<T> calculateValue(List<T> tempValue) {
        return new ArrayList<>(tempValue);
    }

    @Override
    public ListState<T> consumeRow(ListState<T> listState, ResultSet resultSet) throws SQLException {
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
        final List<T> value;
        final List<T> tempValue;
        final PredicateState newPredicateState;
        if(completed) {
            value = new ArrayList<>(listState.getTempValue());
            tempValue = new ArrayList<>();
            newPredicateState = listCompletePredicate.newState(resultSet);
        } else {
            value = listState.getValue();
            tempValue = new ArrayList<>(listState.getTempValue());
            newPredicateState = predicateState;
        }
        //
        if(newestChildState.isCompleted()) {
            tempValue.add(newestChildState.getValue());
        }
        return new ListState<>(completed, value, tempValue, newestChildState, newPredicateState);
    }

    @Override
    public ListState<T> getNewState(ListState<T> oldState) {
        if(oldState == null) {
            return new ListState<>(new ArrayList<T>(), innerResultSetExtractor.getNewState(null), null);
        } else {
            return new ListState<>(oldState.getTempValue(), innerResultSetExtractor.getNewState(oldState.getChildState()), null);
        }
    }
}
