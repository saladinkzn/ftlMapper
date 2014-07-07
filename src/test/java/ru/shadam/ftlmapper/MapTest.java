package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.KeyExtractor;
import ru.shadam.ftlmapper.annotations.query.Query;
import ru.shadam.ftlmapper.entity.PropertyLolInfo;

import java.util.Map;

/**
 * @author sala
 */
public class MapTest extends BaseTest {
    private static interface SimpleMapRepository {
        @Query("select id, name from lol")
        @KeyExtractor("id")
        public Map<Long, PropertyLolInfo> getLolMap();
    }

    private static interface SimpleMapRepository2 {
        @Query("select name, id from lol")
        @KeyExtractor("id")
        public Map<Long, PropertyLolInfo> getLolMap();
    }

    @Test
    public void test() {
        final SimpleMapRepository simpleMapRepository = repositoryFactory.getMapper(SimpleMapRepository.class);
        final Map<Long, PropertyLolInfo> result = simpleMapRepository.getLolMap();
        Assert.assertEquals(2L, result.size());
        Assert.assertNotNull(result.get(1L));
        {
            final PropertyLolInfo first = result.get(1L);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("abc", first.getName());
        }
        {
            final PropertyLolInfo second = result.get(2L);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("def", second.getName());
        }
    }

    @Test
    public void test2() {
        final SimpleMapRepository2 simpleMapRepository = repositoryFactory.getMapper(SimpleMapRepository2.class);
        final Map<Long, PropertyLolInfo> result = simpleMapRepository.getLolMap();
        Assert.assertEquals(2L, result.size());
        Assert.assertNotNull(result.get(1L));
        {
            final PropertyLolInfo first = result.get(1L);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("abc", first.getName());
        }
        {
            final PropertyLolInfo second = result.get(2L);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("def", second.getName());
        }
    }
}
