package ru.shadam.ftlmapper.extractor.state;

import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;

import java.util.Collection;

/**
 *
 */
public class CollectionState<T, U extends Collection<T>> extends ChildPredicateState<T, U> {
    public CollectionState(U tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super(tempValue, childState, predicateState);
    }

    public CollectionState(boolean completed, U value, U tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super(completed, value, tempValue, childState, predicateState);
    }
}
