package lol;

import ru.shadam.ftlmapper.RepositoryFactory;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class LolTest {
    public static void main(String[] args) throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        final RepositoryFactory repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
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
