package lol7;

import ru.shadam.ftlmapper.RepositoryFactory;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

/**
 * @author Timur Shakurov
 */
public class LolTest {
    public static void main(String[] args) throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        final RepositoryFactory repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
        final LolRepository repositoryFactoryMapper = repositoryFactory.getMapper(LolRepository.class);
        final long result = repositoryFactoryMapper.getIdByName("abc");
        System.out.println(result);
    }
}
