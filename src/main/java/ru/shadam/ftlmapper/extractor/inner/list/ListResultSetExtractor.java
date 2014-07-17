package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.CollectionState;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sala
 */
public class ListResultSetExtractor<T> extends CollectionResultSetExtractor<T, List<T>, CollectionState<T, List<T>>> {
    public ListResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor, ResultSetPredicate<? extends PredicateState> listCompletePredicate) {
        super(innerResultSetExtractor, listCompletePredicate);
    }

    @Override
    public List<T> newValue() {
        return new ArrayList<>();
    }

    @Override
    public List<T> newValue(List<T> oldValue) {
        return new ArrayList<>(oldValue);
    }

    @Override
    protected List<T> addNewValue(List<T> tempValue, T childValue) {
        tempValue.add(childValue);
        return tempValue;
    }

    @Override
    protected CollectionState<T, List<T>> newState(List<T> tempValue, SimpleState<T> state, PredicateState predicateState) {
        return new CollectionState<>(tempValue, state, predicateState);
    }

    @Override
    protected CollectionState<T, List<T>> newState(boolean completed, List<T> value, List<T> tempValue, SimpleState<T> state, PredicateState predicateState) {
        return new CollectionState<>(completed, value, tempValue, state, predicateState);

    }
}
