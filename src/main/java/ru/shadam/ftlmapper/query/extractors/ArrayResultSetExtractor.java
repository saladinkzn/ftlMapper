package ru.shadam.ftlmapper.query.extractors;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.query.ResultSetExtractor;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sala
 */
public class ArrayResultSetExtractor implements ResultSetExtractor<Object> {
    private Class<?> clazz;
    private final RowMapper<?> rowMapper;

    public ArrayResultSetExtractor(RowMapper<?> rowMapper, Class<?> clazz) {
        this.rowMapper = rowMapper;
        this.clazz = clazz;
    }

    @Override
    public Object extractResult(ResultSet resultSet) throws SQLException {
        final List<Object> resultList = new ArrayList<>();
        final ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
        while (resultSet.next()) {
            resultList.add(rowMapper.mapRow(resultSetWrapper));
        }
        final Object array = Array.newInstance(clazz, resultList.size());
        for(int i = 0; i < resultList.size(); i++) {
            Array.set(array, i, resultList.get(i));
        }
        return array;
    }
}
