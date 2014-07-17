package ru.shadam.ftlmapper.extractor.state;

/**
 *
 */
public class SimpleState<T> {
    private final boolean completed;
    private final T value;

    public SimpleState() {
        this(false, null);
    }

    public SimpleState(boolean completed, T value) {
        this.completed = completed;
        this.value = value;
    }

    public boolean isCompleted() {
        return completed;
    }

    public T getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "SimpleState{" +
                "completed=" + completed +
                ", value=" + value +
                '}';
    }
}
