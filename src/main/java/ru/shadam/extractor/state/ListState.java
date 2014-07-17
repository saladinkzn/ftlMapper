package ru.shadam.extractor.state;

import ru.shadam.extractor.inner.list.predicates.PredicateState;

import java.util.List;

/**
 *
 */
public class ListState<T> extends SimpleState<List<T>> {
    private final List<T> tempValue;
    private final SimpleState<T> childState;
    //
    private final PredicateState predicateState;

    public ListState(List<T> tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super();
        this.tempValue = tempValue;
        this.childState = childState;
        this.predicateState = predicateState;
    }

    public ListState(boolean completed, List<T> value, List<T> tempValue, SimpleState<T> childState, PredicateState predicateState) {
        super(completed, value);
        this.tempValue = tempValue;
        this.childState = childState;
        this.predicateState = predicateState;
    }

    public List<T> getTempValue() {
        return tempValue;
    }

    public SimpleState<T> getChildState() {
        return childState;
    }

    public PredicateState getPredicateState() {
        return predicateState;
    }

    @Override
    public String toString() {
        return "ListState{" +
                "completed="+ isCompleted() +
                ", value=" + getValue() +
                ", tempValue=" + tempValue +
                ", childState=" + childState +
                '}';
    }
}
