package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.shadam.ftlmapper.entity.CreatorLolInfo;
import ru.shadam.ftlmapper.entity.PropertyLolInfo;
import ru.shadam.ftlmapper.query.annotations.Query;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class MappedTypeTest {
    public static interface LolRepository {
        @Query("select id, name from lol")
        public List<CreatorLolInfo> getAll();

        @Query("select id, name from lol")
        public List<PropertyLolInfo> getAllProperty();
    }

    private static RepositoryFactory repositoryFactory;

    @BeforeClass
    public static void init() throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
    }

    @Test
    public void test() throws Exception {
        final LolRepository repository = repositoryFactory.getMapper(LolRepository.class);
        final List<CreatorLolInfo> all = repository.getAll();
        Assert.assertEquals(2, all.size());
        final CreatorLolInfo first = all.get(0);
        Assert.assertEquals(1L, first.getId());
        Assert.assertEquals("abc", first.getName());
        //
        final CreatorLolInfo second = all.get(1);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());
    }

    @Test
    public void test2() throws Exception {
        final LolRepository repository = repositoryFactory.getMapper(LolRepository.class);
        final List<PropertyLolInfo> all = repository.getAllProperty();
        Assert.assertEquals(2, all.size());
        final PropertyLolInfo first = all.get(0);
        Assert.assertEquals(1L, first.getId());
        Assert.assertEquals("abc", first.getName());
        final PropertyLolInfo second = all.get(1);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());
    }
}
