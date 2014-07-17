package ru.shadam.ftlmapper;

import entity.creator.Master;
import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Mapper;
import ru.shadam.ftlmapper.annotations.query.Param;
import ru.shadam.ftlmapper.annotations.query.Template;
import ru.shadam.ftlmapper.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class MapperTest extends BaseTest {

    public interface LolRepository {
        @Template("sql/lols/getAll.ftl")
        @Mapper(LolInfoListMapper.class)
        public List<Master> getLols(@Param("lolcount") long count);

        @Template("sql/lols/getOne.ftl")
        @Mapper(LolInfoMapper.class)
        public Master getLolInfo(@Param("id") long id);
    }

    @Test
    public void test() {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        final List<Master> lols = lolRepository.getLols(1);
        Assert.assertEquals(1, lols.size());
        Assert.assertEquals(1, lols.get(0).getId());
        Assert.assertEquals("abc", lols.get(0).getName());
        //
        final Master lolInfo = lolRepository.getLolInfo(2L);
        Assert.assertNotNull(lolInfo);
        Assert.assertEquals(2L, lolInfo.getId());
        Assert.assertEquals("def", lolInfo.getName());
    }

    /**
     * simple mapper
     */
    public static class LolInfoMapper implements ResultSetExtractor<Master> {

        @Override
        public Master extractResult(ResultSet resultSet) throws SQLException {
            if(resultSet.next()) {
                return new Master(resultSet.getLong(1), resultSet.getString(2));
            } else {
                return null;
            }
        }
    }

    /**
     * list mapper
     */
    public static class LolInfoListMapper implements ResultSetExtractor<List<Master>> {

        @Override
        public List<Master> extractResult(ResultSet resultSet) throws SQLException {
            final List<Master> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new Master(resultSet.getLong(1), resultSet.getString(2)));
            }
            return result;
        }
    }
}
