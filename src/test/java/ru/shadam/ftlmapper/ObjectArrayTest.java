package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.List;

/**
 * @author sala
 */
public class ObjectArrayTest extends BaseTest {
    static interface Repository {
        @Query("select id, name from master limit 1")
        Object[] getFirst();
    }
    static interface Repository2 {
        @Query("select id, name from master")
        List<Object[]> getAll();
    }

    @Test
    public void test() {
        final Repository repository = repositoryFactory.getMapper(Repository.class);
        final Object[] first = repository.getFirst();
        Assert.assertEquals(1L, first[0]);
        Assert.assertEquals("abc", first[1]);
    }

    @Test
    public void test2() {
        final Repository2 repository2 = repositoryFactory.getMapper(Repository2.class);
        final List<Object[]> result = repository2.getAll();
        Assert.assertEquals(2, result.size());
        //
        {
            final Object[] first = result.get(0);
            Assert.assertEquals(1L, first[0]);
            Assert.assertEquals("abc", first[1]);
        }
        //
        {
            final Object[] second = result.get(1);
            Assert.assertEquals(2L, second[0]);
            Assert.assertEquals("def", second[1]);
        }
    }
}
