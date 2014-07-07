package ru.shadam.ftlmapper.query.extractors;

import ru.shadam.ftlmapper.mapper.RowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sala
 */
public class ListResultSetExtractor<T> extends CollectionResultSetExtractor<T, List<T>> {
    public ListResultSetExtractor(RowMapper<T> rowMapper) {
        super(rowMapper);
    }

    @Override
    public List<T> getCollection() {
        return new ArrayList<>();
    }

}
