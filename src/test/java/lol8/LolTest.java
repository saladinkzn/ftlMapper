package lol8;

import org.junit.Assert;
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
    @Test
    public void testName() throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        final RepositoryFactory repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        final Set<Long> ids = lolRepository.getIds();
        final Iterator<Long> iterator = ids.iterator();
        final Long first = iterator.next();
        Assert.assertEquals(1L, first.longValue());
        final Long second = iterator.next();
        Assert.assertEquals(2L, second.longValue());
        System.out.println("ids: " + ids.toString());
    }
}
