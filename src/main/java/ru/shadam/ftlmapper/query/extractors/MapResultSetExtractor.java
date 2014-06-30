package ru.shadam.ftlmapper.query.extractors;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sala
 */
public class MapResultSetExtractor<K, V> implements ResultSetExtractor<Map<K, V>> {
    private final RowMapper<K> keyRowMapper;
    private final RowMapper<V> valueRowMapper;

    public MapResultSetExtractor(RowMapper<K> keyRowMapper, RowMapper<V> valueRowMapper) {
        this.keyRowMapper = keyRowMapper;
        this.valueRowMapper = valueRowMapper;
    }

    @Override
    public Map<K, V> extractResult(ResultSet resultSet) throws SQLException {
        final Map<K, V> result = new HashMap<>();
        final ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
        while (resultSet.next()) {
            result.put(keyRowMapper.mapRow(resultSetWrapper), valueRowMapper.mapRow(resultSetWrapper));
        }
        return result;
    }
}
