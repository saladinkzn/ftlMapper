package lol2;

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
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        for(LolInfo lolInfo: lolRepository.getLols()) {
            System.out.println(lolInfo);
        }
    }

}
