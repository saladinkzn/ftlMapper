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
    @SuppressWarnings("unchecked")
    public static DataSourceAdapter getDataSourceAdapter() throws ClassNotFoundException, SQLException {
        final SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass((Class<Driver>)Class.forName("org.h2.Driver"));
        simpleDriverDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        simpleDriverDataSource.setUsername("test");
        simpleDriverDataSource.setPassword("test");
        try (Connection connection = simpleDriverDataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate("create table if not exists lol (id bigint primary key, name varchar(50))");
                statement.executeUpdate("create table if not exists lol_extra (id bigint references lol(id), extra_field varchar(50))");
                statement.executeUpdate("delete from lol_extra");
                statement.executeUpdate("delete from lol");
                try {
                    statement.executeUpdate("INSERT INTO lol VALUES (1, 'abc'), (2, 'def')");
                } catch (Exception ex) {
                    // TODO: logging for test
                    ex.printStackTrace();
                }
                try {
                    statement.executeUpdate("INSERT INTO lol_extra VALUES (1, 'extra1'), (2, 'extra2')");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return new DataSourceAdapter(simpleDriverDataSource);
    }
}
