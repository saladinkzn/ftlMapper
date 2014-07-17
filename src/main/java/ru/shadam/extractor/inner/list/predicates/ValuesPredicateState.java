package ru.shadam.extractor.inner.list.predicates;

import java.util.List;

/**
 * @author sala
 */
public class ValuesPredicateState extends PredicateState {
    private final List<Object> values;

    public ValuesPredicateState(List<Object> values) {
        this.values = values;
    }

    public List<Object> getValues() {
        return values;
    }
}
