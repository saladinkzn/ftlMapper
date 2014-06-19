package ru.shadam.mapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
