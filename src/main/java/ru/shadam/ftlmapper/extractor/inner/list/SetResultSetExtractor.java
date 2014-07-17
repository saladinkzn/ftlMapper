package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.CollectionState;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 */
public class SetResultSetExtractor<T> extends CollectionResultSetExtractor<T, Set<T>, CollectionState<T, Set<T>>> {
    public SetResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor, ResultSetPredicate<? extends PredicateState> listCompletePredicate) {
        super(innerResultSetExtractor, listCompletePredicate);
    }

    @Override
    public Set<T> newValue() {
        return new LinkedHashSet<>();
    }

    @Override
    public Set<T> newValue(Set<T> oldValue) {
        return new LinkedHashSet<>(oldValue);
    }

    protected Set<T> addNewValue(Set<T> tempValue, T childValue) {
        final Set<T> newTempValue;
        tempValue.add(childValue);
        newTempValue = tempValue;
        return newTempValue;
    }

    @Override
    protected CollectionState<T, Set<T>> newState(Set<T> newValue, SimpleState<T> state, PredicateState predicateState) {
        return new CollectionState<>(newValue, state, predicateState);
    }

    @Override
    protected CollectionState<T, Set<T>> newState(boolean completed, Set<T> value, Set<T> newValue, SimpleState<T> state, PredicateState predicateState) {
        return new CollectionState<>(completed, value, newValue, state, predicateState);
    }
}
