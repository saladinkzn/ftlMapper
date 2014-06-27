package ru.shadam.ftlmapper.query.extractors;

import ru.shadam.ftlmapper.mapper.RowMapper;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author sala
 */
public class SetResultSetExtractor<T> extends CollectionResultSetExtractor<T, Set<T>> {
    public SetResultSetExtractor(RowMapper<T> rowMapper) {
        super(rowMapper);
    }

    @Override
    public Set<T> getCollection() {
        return new LinkedHashSet<>();
    }
}
