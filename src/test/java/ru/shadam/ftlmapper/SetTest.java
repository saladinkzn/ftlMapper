package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;
import ru.shadam.ftlmapper.entity.CreatorLolInfo;

import java.util.Iterator;
import java.util.Set;

/**
 * @author sala
 */
public class SetTest extends BaseTest {
    static interface Repository {
        @Query("select id, name from lol")
        Set<CreatorLolInfo> getAll();
    }

    @Test
    public void test() {
        final Repository repository = repositoryFactory.getMapper(Repository.class);
        final Set<CreatorLolInfo> all = repository.getAll();
        final Iterator<CreatorLolInfo> iterator = all.iterator();
        Assert.assertTrue(iterator.hasNext());
        final CreatorLolInfo first = iterator.next();
        Assert.assertNotNull(first);
        Assert.assertEquals(1L, first.getId());
        Assert.assertEquals("abc", first.getName());
        //
        Assert.assertTrue(iterator.hasNext());
        final CreatorLolInfo second = iterator.next();
        Assert.assertNotNull(second);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());

    }
}
