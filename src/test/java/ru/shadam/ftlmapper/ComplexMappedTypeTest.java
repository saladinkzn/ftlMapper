package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.entity.ComplexLolInfo;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ComplexMappedTypeTest extends BaseTest {
    public static interface ComplexMappedRepository {
        // Haha I'm too lazy to create a new table ;)
        @Query("select id, id as ch_id, name as ch_name from lol")
        List<ComplexLolInfo> getAllComplexLolInfo();
    }

    @Test
    public void testComplexTest() {
        final ComplexMappedRepository complexMappedRepository = repositoryFactory.getMapper(ComplexMappedRepository.class);
        final List<ComplexLolInfo> result = complexMappedRepository.getAllComplexLolInfo();
        Assert.assertEquals(result.size(), 2);
        final ComplexLolInfo first = result.get(0);
        Assert.assertEquals(1L, first.getId());
        Assert.assertNotNull(first.getEmbedded());
        Assert.assertEquals(1L, first.getEmbedded().getId());
        Assert.assertEquals("abc", first.getEmbedded().getName());
        //
        final ComplexLolInfo second = result.get(1);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals(2L, second.getEmbedded().getId());
        Assert.assertEquals("def", second.getEmbedded().getName());
        System.out.println(result);
    }
}
