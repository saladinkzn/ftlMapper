package ru.shadam.ftlmapper;

import entity.creator.Master;
import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class MappedTypeTest extends BaseTest {
    public static interface LolRepository {
        @Query("select id, name from master")
        public List<Master> getAll();
    }

    @Test
    public void test() throws Exception {
        final LolRepository repository = repositoryFactory.getMapper(LolRepository.class);
        final List<Master> all = repository.getAll();
        Assert.assertEquals(2, all.size());
        final Master first = all.get(0);
        Assert.assertEquals(1L, first.getId());
        Assert.assertEquals("abc", first.getName());
        //
        final Master second = all.get(1);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());
    }
}
