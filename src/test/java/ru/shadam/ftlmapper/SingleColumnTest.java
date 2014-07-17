package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.Iterator;
import java.util.List;

/**
 * @author sala
 */
public class SingleColumnTest extends BaseTest {
    public static interface LolRepository {
        @Query("select id, name from master order by id asc")
        public List<Long> getIds();

        @Query("select name from master order by id asc")
        public List<String> getNames();

        @Query("select id from master order by id asc")
        public Long[] getIdsArray();
    }

    @Test
    public void testIds() throws Exception {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        //
        final List<Long> ids = lolRepository.getIds();
        final Iterator<Long> iterator = ids.iterator();
        final Long first = iterator.next();
        Assert.assertEquals(1L, first.longValue());
        final Long second = iterator.next();
        Assert.assertEquals(2L, second.longValue());
        System.out.println("ids: " + ids.toString());
        //
    }

    @Test
    public void testNames() throws Exception {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        //
        final List<String> names = lolRepository.getNames();
        final Iterator<String> iterator = names.iterator();
        final String first = iterator.next();
        Assert.assertEquals("abc", first);
        final String second = iterator.next();
        Assert.assertEquals("def", second);
        System.out.println("names: " + names.toString());
    }

    @Test
    public void testArray() throws Exception {
        final LolRepository lolRepository = repositoryFactory.getMapper(LolRepository.class);
        //
        final Long[] result = lolRepository.getIdsArray();
        Assert.assertArrayEquals(new Long[] {1L, 2L}, result);
    }
}
