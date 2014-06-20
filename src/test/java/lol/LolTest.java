package lol;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import ru.shadam.ftlmapper.*;
import ru.shadam.ftlmapper.query.QueryInvocationHandler;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;

import java.sql.*;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class LolTest {
    public static void main(String[] args) throws Exception {
        final QueryManager queryManager = new QueryManager();
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
        final DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(simpleDriverDataSource);
        final QueryInvocationHandler queryInvocationHandler = new QueryInvocationHandler(queryManager, dataSourceAdapter);
        final RepositoryFactory repositoryFactory = new RepositoryFactory(queryInvocationHandler);
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        final List<LolInfo> lols = lolRepository.getLols(1);
        for(LolInfo lol: lols) {
            System.out.println(lol);
        }
        //
        final LolInfo lolInfo = lolRepository.getLolInfo(2L);
        System.out.println(lolInfo);
    }
}
