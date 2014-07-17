package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.Iterator;
import java.util.Set;

/**
 * @author sala
 */
public class SetTest extends BaseTest {
    static interface Repository {
        @Query("select id, name from master")
        Set<entity.creator.Master> getAll();
    }

    @Test
    public void test() {
        final Repository repository = repositoryFactory.getMapper(Repository.class);
        final Set<entity.creator.Master> all = repository.getAll();
        final Iterator<entity.creator.Master> iterator = all.iterator();
        Assert.assertTrue(iterator.hasNext());
        final entity.creator.Master first = iterator.next();
        Assert.assertNotNull(first);
        Assert.assertEquals(1L, first.getId());
        Assert.assertEquals("abc", first.getName());
        //
        Assert.assertTrue(iterator.hasNext());
        final entity.creator.Master second = iterator.next();
        Assert.assertNotNull(second);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());

    }
}
