package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.domain.Tuple2;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.List;

/**
 * @author sala
 */
public class TupleTest extends BaseTest {
    private static interface Repository {
        @Query("select id, name from lol")
        public List<Tuple2<Long, String>> getLols();
    }

    @Test
    public void test() {
        final Repository repository = repositoryFactory.getMapper(Repository.class);
        final List<Tuple2<Long, String>> lols = repository.getLols();
        Assert.assertEquals(2, lols.size());
        {
            final Tuple2<Long, String> first = lols.get(0);
            Assert.assertEquals(Long.valueOf(1L), first.getFirst());
            Assert.assertEquals("abc", first.getSecond());
        }
        //
        {
            final Tuple2<Long, String> second = lols.get(1);
            Assert.assertEquals(Long.valueOf(1L), second.getFirst());
            Assert.assertEquals("def", second.getSecond());
        }
    }
}
