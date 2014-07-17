package ru.shadam.ftlmapper;

import entity.creator.Master;
import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Param;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ParameterTest extends BaseTest {
    public static interface LolRepository {
        @Query("select id from master where name = ?1")
        public long getIdByName(@Param("name") String name);

        @Query("select id, name from master where id = ?1 and name = ?2")
        public List<Master> getOne(@Param("id") long id, @Param("name") String name);
    }

    @Test
    public void test1() throws Exception {
        final LolRepository repositoryFactoryMapper = repositoryFactory.getMapper(LolRepository.class);
        Assert.assertEquals(1L, repositoryFactoryMapper.getIdByName("abc"));
    }

    @Test
    public void test2() throws Exception {
        final LolRepository repository = repositoryFactory.getMapper(LolRepository.class);
        final List<Master> abc = repository.getOne(1L, "abc");
        Assert.assertEquals(1L, abc.size());
        Assert.assertEquals(1L, abc.get(0).getId());
        Assert.assertEquals("abc", abc.get(0).getName());
    }
}
