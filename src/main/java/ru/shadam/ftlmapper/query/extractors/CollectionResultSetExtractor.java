package ru.shadam.ftlmapper.query.extractors;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author sala
 */
public abstract class CollectionResultSetExtractor<T, U extends Collection<T>> implements ResultSetExtractor<U> {
    protected RowMapper<T> rowMapper;

    public abstract U getCollection();

    protected CollectionResultSetExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public U extractResult(ResultSet resultSet) throws SQLException {
        final U collection = getCollection();
        while (resultSet.next()) {
            collection.add(rowMapper.mapRow(resultSet));
        }
        return collection;
    }
}
