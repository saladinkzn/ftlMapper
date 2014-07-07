package ru.shadam.ftlmapper.util;

import ru.shadam.ftlmapper.query.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public <T> T query(PreparedStatementCreator preparedStatementCreator, ResultSetExtractor<T> resultSetExtractor) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = preparedStatementCreator.prepareStatement(connection)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    return resultSetExtractor.extractResult(resultSet);
                }
            }
        }
    }
}
