package lol;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.shadam.ftlmapper.RepositoryFactory;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class LolTest {
    private static RepositoryFactory repositoryFactory;

    @BeforeClass
    public static void init() throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
    }

    @Test
    public void test() throws Exception {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        final List<LolInfo> lols = lolRepository.getLols(1);
        Assert.assertEquals(1, lols.size());
        Assert.assertEquals(1, lols.get(0).getId());
        Assert.assertEquals("abc", lols.get(0).getName());
        //
        final LolInfo lolInfo = lolRepository.getLolInfo(2L);
        Assert.assertNotNull(lolInfo);
        Assert.assertEquals(2L, lolInfo.getId());
        Assert.assertEquals("def", lolInfo.getName());
    }
}
