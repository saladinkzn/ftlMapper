package ru.shadam.ftlmapper.extractor.state;

import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;

import java.util.Collection;

/**
 *
 */
public class CollectionState<T, U extends Collection<T>> extends SimpleState<U> {
    private final U tempValue;
    private final SimpleState<T> childState;
    private final PredicateState predicateState;

    public CollectionState(U tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super();
        this.tempValue = tempValue;
        this.childState = childState;
        this.predicateState = predicateState;
    }

    public CollectionState(boolean completed, U value, U tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super(completed, value);
        this.tempValue = tempValue;
        this.childState = childState;
        this.predicateState = predicateState;
    }

    public U getTempValue() {
        return tempValue;
    }

    public SimpleState<T> getChildState() {
        return childState;
    }

    public PredicateState getPredicateState() {
        return predicateState;
    }
}
