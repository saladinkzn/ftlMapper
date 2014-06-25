package ru.shadam.ftlmapper.util;

import ru.shadam.ftlmapper.mapper.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Class for executing database queries
 *
 * @author Timur Shakurov
 */
public class DataSourceAdapter {
    private DataSource dataSource;

    public DataSourceAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // TODO: templateName + params -> SQL
    public <T> List<T> query(String sql, RowMapper<T> mapper) throws SQLException {
        return query(sql, mapper, new Supplier<List<T>>() {
            @Override
            public List<T> get() {
                return new ArrayList<>();
            }
        });
    }

    public <T> Set<T> queryForSet(String sql, RowMapper<T> mapper) throws SQLException {
        return query(sql, mapper, new Supplier<Set<T>>() {
            @Override
            public Set<T> get() {
                return new LinkedHashSet<>();
            }
        });
    }

    public <T, U extends Collection<T>> U query(String sql, RowMapper<T> mapper, Supplier<U> supplier) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    final U result = supplier.get();
                    while(resultSet.next()) {
                        final T mapped = mapper.mapRow(resultSet);
                        result.add(mapped);
                    }
                    return result;
                }
            }
        }
    }

    public <T> T uniqueQuery(String sql, RowMapper<T> rowMapper) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    if(!resultSet.next()) {
                        return null;
                    }
                    final T result = rowMapper.mapRow(resultSet);
                    if(resultSet.next()) {
                        throw new RuntimeException("query returns more than one result row");
                    } else {
                        return result;
                    }
                }
            }
        }
    }
}
