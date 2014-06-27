package ru.shadam.ftlmapper.query.extractors;

import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sala
 */
public class UniqueResultExtractor<T> implements ResultSetExtractor<T> {
    private final RowMapper<T> rowMapper;

    public UniqueResultExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T extractResult(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            return rowMapper.mapRow(resultSet);
        } else {
            return null;
        }
    }
}
