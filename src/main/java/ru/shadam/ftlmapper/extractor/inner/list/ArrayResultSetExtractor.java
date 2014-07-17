package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.ArrayState;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author sala
 */
public class ArrayResultSetExtractor<T> extends CollectionResultSetExtractor<T, T[], ArrayState<T>> {
    private final Class<T> clazz;

    public ArrayResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor,
                                   ResultSetPredicate<? extends PredicateState> listCompletePredicate,
                                   Class<T> clazz) {
        super(innerResultSetExtractor, listCompletePredicate);
        this.clazz = clazz;
    }

    @Override
    protected T[] newValue() {
        return (T[]) Array.newInstance(clazz, 0);
    }

    @Override
    protected T[] newValue(T[] oldValue) {
        return Arrays.copyOf(oldValue, oldValue.length);
    }

    @Override
    protected T[] addNewValue(T[] tempValue, T childValue) {
        T[] copy = Arrays.copyOf(tempValue, tempValue.length + 1);
        copy[tempValue.length] = childValue;
        return copy;
    }

    @Override
    protected ArrayState<T> newState(T[] newValue, SimpleState<T> state, PredicateState predicateState) {
        return new ArrayState<T>(newValue, state, predicateState);
    }

    @Override
    protected ArrayState<T> newState(boolean completed, T[] value, T[] newValue, SimpleState<T> state, PredicateState predicateState) {
        return new ArrayState<T>(completed, value, newValue, state, predicateState);
    }
}
