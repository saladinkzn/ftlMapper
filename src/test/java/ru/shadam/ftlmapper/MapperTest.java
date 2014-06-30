package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.shadam.ftlmapper.entity.SimpleLolInfo;
import ru.shadam.ftlmapper.mapper.ResultSetWrapper;
import ru.shadam.ftlmapper.mapper.RowMapper;
import ru.shadam.ftlmapper.annotations.query.Mapper;
import ru.shadam.ftlmapper.annotations.query.Param;
import ru.shadam.ftlmapper.annotations.query.Template;
import ru.shadam.ftlmapper.util.DataSourceAdapter;
import ru.shadam.ftlmapper.util.QueryManager;
import util.TestHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class MapperTest {
    private static RepositoryFactory repositoryFactory;

    public interface LolRepository {
        @Template("sql/lols/getAll.ftl")
        @Mapper(LolInfoMapper.class)
        public List<SimpleLolInfo> getLols(@Param("lolcount") long count);

        @Template("sql/lols/getOne.ftl")
        @Mapper(LolInfoMapper.class)
        public SimpleLolInfo getLolInfo(@Param("id") long id);
    }


    @BeforeClass
    public static void init() throws Exception {
        final QueryManager queryManager = new QueryManager();
        final DataSourceAdapter dataSourceAdapter = TestHelper.getDataSourceAdapter();
        repositoryFactory = new RepositoryFactory(queryManager, dataSourceAdapter);
    }

    @Test
    public void test() {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        final List<SimpleLolInfo> lols = lolRepository.getLols(1);
        Assert.assertEquals(1, lols.size());
        Assert.assertEquals(1, lols.get(0).getId());
        Assert.assertEquals("abc", lols.get(0).getName());
        //
        final SimpleLolInfo lolInfo = lolRepository.getLolInfo(2L);
        Assert.assertNotNull(lolInfo);
        Assert.assertEquals(2L, lolInfo.getId());
        Assert.assertEquals("def", lolInfo.getName());
    }

    /**
     * simple mapper
     */
    public static class LolInfoMapper implements RowMapper<SimpleLolInfo> {

        @Override
        public SimpleLolInfo mapRow(ResultSetWrapper resultSet) throws SQLException {
            return new SimpleLolInfo(resultSet.getLong(1), resultSet.getString(2));
        }
    }
}
