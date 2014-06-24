package lol2;

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
        final List<LolInfo> lols = lolRepository.getLols();
        Assert.assertEquals(2, lols.size());
        final LolInfo first = lols.get(0);
        Assert.assertEquals(1, first.getId());
        Assert.assertEquals("abc", first.getName());
        //
        final LolInfo second = lols.get(1);
        Assert.assertEquals(2, second.getId());
        Assert.assertEquals("def", second.getName());
    }

}
