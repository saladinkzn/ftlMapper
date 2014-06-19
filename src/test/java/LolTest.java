import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import ru.shadam.mapper.*;

import java.sql.*;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class LolTest {
    /**
     * Simple pojo
     */
    public static class LolInfo {
        private long id;
        private String name;

        public LolInfo(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("LolInfo{");
            sb.append("id=").append(id);
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * simple mapper
     */
    public static class LolInfoMapper implements RowMapper<LolInfo> {

        @Override
        public LolInfo mapRow(ResultSet resultSet) throws SQLException {
            return new LolInfo(resultSet.getLong(1), resultSet.getString(2));
        }
    }

    /**
     * simple repository
     */
    public static interface LolRepository {
        @Query(templateName = "sql/lols/getAll.ftl", mapper = LolInfoMapper.class)
        public List<LolInfo> getLols(@Param("lolcount") long count);
    }

    public static void main(String[] args) throws Exception {
        QueryManager queryManager = new QueryManager();
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
        final DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(queryManager, simpleDriverDataSource);

        Mapper mapper = new Mapper(dataSourceAdapter);
        final LolRepository lolRepository = mapper.getMapper(LolRepository.class);
        final List<LolInfo> lols = lolRepository.getLols(5);
        for(LolInfo lol: lols) {
            System.out.println(lol);
        }
    }
}
