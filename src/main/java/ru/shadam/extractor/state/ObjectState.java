package ru.shadam.extractor.state;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ObjectState<T> extends SimpleState<T> {
    private Map<String, Object> values;
    private Map<String, SimpleState<?>> childStates;

    public ObjectState(Map<String, SimpleState<?>> childStates) {
        this(false, null, new HashMap<String, Object>(), childStates);
    }

    public ObjectState(boolean completed, T value, Map<String, Object> values, Map<String, SimpleState<?>> childStates) {
        super(completed, value);
        this.values = values;
        this.childStates = childStates;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public Map<String, SimpleState<?>> getChildStates() {
        return childStates;
    }


    @Override
    public String toString() {
        return "ObjectState{" +
                "completed=" + isCompleted() +
                ", value=" + getValue() +
                ", values=" + values +
                ", childStates=" + childStates +
                '}';
    }
}
