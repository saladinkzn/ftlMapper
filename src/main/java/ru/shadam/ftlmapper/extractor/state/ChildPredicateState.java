package ru.shadam.ftlmapper.extractor.state;

import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;

/**
 * @author sala
 */
public class ChildPredicateState<T, U> extends SimpleState<U> {
    private final U tempValue;
    private final SimpleState<T> childState;
    private final PredicateState predicateState;

    public ChildPredicateState(U tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super();
        this.tempValue = tempValue;
        this.childState = childState;
        this.predicateState = predicateState;
    }

    public ChildPredicateState(boolean completed, U value, U tempValue, SimpleState<T> childState, PredicateState predicateState) {
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
