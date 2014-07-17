package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 */
public class SetResultSetExtractor<T> extends CollectionResultSetExtractor<T, Set<T>> {
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
}
