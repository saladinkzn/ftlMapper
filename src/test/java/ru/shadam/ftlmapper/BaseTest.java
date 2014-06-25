package ru.shadam.ftlmapper;

import org.junit.BeforeClass;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

/**
 * @author Timur Shakurov
 */
public class BaseTest {
    protected static RepositoryFactory repositoryFactory;
    //
    @BeforeClass
    public static void init() throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
    }
}
