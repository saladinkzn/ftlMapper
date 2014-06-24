package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.shadam.ftlmapper.entity.CreatorLolInfo;
import ru.shadam.ftlmapper.query.annotations.Template;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class TemplateTest {
    public static interface LolRepository {
        @Template("sql/lol4/getAll.ftl")
        public List<CreatorLolInfo> getAll();
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
        final CreatorLolInfo second = all.get(1);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());
    }
}
