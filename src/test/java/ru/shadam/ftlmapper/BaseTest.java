package ru.shadam.ftlmapper;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;

/**
 * @author Timur Shakurov
 */
public abstract class BaseTest {
    protected RepositoryFactory repositoryFactory;
    private EmbeddedDatabase embeddedDatabase;

    //
    @Before
    public void init() throws Exception {
        final QueryManager queryManager = new QueryManager();
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addDefaultScripts()
                .build();
        final DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(embeddedDatabase);
        repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
    }

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }
}
