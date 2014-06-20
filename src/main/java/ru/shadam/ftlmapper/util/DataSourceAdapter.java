package ru.shadam.ftlmapper.util;

import ru.shadam.ftlmapper.mapper.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        try(Connection connection = dataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    List<T> resultList = new ArrayList<>();
                    while (resultSet.next()) {
                        final T mapped = mapper.mapRow(resultSet);
                        resultList.add(mapped);
                    }
                    return resultList;
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
