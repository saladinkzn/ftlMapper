package util;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import ru.shadam.ftlmapper.util.DataSourceAdapter;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Timur Shakurov
 */
public abstract class TestHelper {

    public static DataSourceAdapter getDataSourceAdapter() throws ClassNotFoundException, SQLException {
        final SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass((Class<Driver>)Class.forName("org.h2.Driver"));
        simpleDriverDataSource.setUrl("jdbc:h2:~/test");
        simpleDriverDataSource.setUsername("test");
        simpleDriverDataSource.setPassword("test");
        try (Connection connection = simpleDriverDataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate("create table if not exists lol (id bigint primary key, name varchar(50))");
                try {
                    statement.executeUpdate("INSERT INTO lol VALUES (1, 'abc'), (2, 'def')");
                } catch (Exception ex) {}
            }
        }
        return new DataSourceAdapter(simpleDriverDataSource);
    }
}
