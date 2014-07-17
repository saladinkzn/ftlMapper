package ru.shadam.ftlmapper.extractor.state;

import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;

/**
 * @author sala
 */
public class ArrayState<T> extends ChildPredicateState<T, T[]> {

    public ArrayState(T[] tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super(tempValue, childState, predicateState);
    }

    public ArrayState(boolean completed, T[] value, T[] tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super(completed, value, tempValue, childState, predicateState);
    }
}