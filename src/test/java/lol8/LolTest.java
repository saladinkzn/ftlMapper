package lol8;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.shadam.ftlmapper.RepositoryFactory;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

import java.util.Iterator;
import java.util.Set;

/**
 * @author sala
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
    public void testIds() throws Exception {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        //
        final Set<Long> ids = lolRepository.getIds();
        final Iterator<Long> iterator = ids.iterator();
        final Long first = iterator.next();
        Assert.assertEquals(1L, first.longValue());
        final Long second = iterator.next();
        Assert.assertEquals(2L, second.longValue());
        System.out.println("ids: " + ids.toString());
        //
    }

    @Test
    public void testNames() throws Exception {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        //
        final Set<String> names = lolRepository.getNames();
        final Iterator<String> iterator = names.iterator();
        final String first = iterator.next();
        Assert.assertEquals("abc", first);
        final String second = iterator.next();
        Assert.assertEquals("def", second);
        System.out.println("names: " + names.toString());
    }
}
