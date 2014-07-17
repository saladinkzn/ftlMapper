package ru.shadam.ftlmapper.extractor.inner.list;

import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.PredicateState;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sala
 */
public class ListResultSetExtractor<T> extends CollectionResultSetExtractor<T, List<T>> {
    public ListResultSetExtractor(InnerResultSetExtractor<? extends SimpleState<T>> innerResultSetExtractor, ResultSetPredicate<? extends PredicateState> listCompletePredicate) {
        super(innerResultSetExtractor, listCompletePredicate);
    }

    @Override
    public List<T> newValue() {
        return new ArrayList<>();
    }

    @Override
    public List<T> newValue(List<T> oldValue) {
        return new ArrayList<>(oldValue);
    }
}
