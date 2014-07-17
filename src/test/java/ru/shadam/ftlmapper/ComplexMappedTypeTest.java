package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.entity.ComplexLolInfo;
import ru.shadam.ftlmapper.annotations.query.Query;
import ru.shadam.ftlmapper.entity.CreatorComplexLolInfo;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ComplexMappedTypeTest extends BaseTest {
    public static interface ComplexMappedRepository {
        // Haha I'm too lazy to create a new table ;)
        @Query("select id, id as ch_id, name as ch_name from lol")
        List<ComplexLolInfo> getAllComplexLolInfo();

        // Haha I'm too lazy to create a new table ;)
        @Query("select id, id as ch_id, name as ch_name from lol")
        List<CreatorComplexLolInfo> getAllComplexLolInfo2();
    }

    public static interface ComplexMappedRepository2 {
        @Query("select l.id, l.name, le.extra_field as extra_extraInfo from lol l join lol_extra le on l.id = le.id")
        public List<CreatorComplexLolInfo> getAllComplexLolInfo();
    }

    @Test
    public void testComplexTest() {
        final ComplexMappedRepository complexMappedRepository = repositoryFactory.getMapper(ComplexMappedRepository.class);
        final List<ComplexLolInfo> result = complexMappedRepository.getAllComplexLolInfo();
        Assert.assertEquals(result.size(), 2);
        final ComplexLolInfo first = result.get(0);
        Assert.assertEquals(1L, first.id);
        Assert.assertNotNull(first.embedded);
        Assert.assertEquals(1L, first.embedded.getId());
        Assert.assertEquals("abc", first.embedded.getName());
        //
        final ComplexLolInfo second = result.get(1);
        Assert.assertEquals(2L, second.id);
        Assert.assertEquals(2L, second.embedded.getId());
        Assert.assertEquals("def", second.embedded.getName());
        System.out.println(result);
    }

    @Test
    public void testComplexTest2() {
        final ComplexMappedRepository2 complexMappedRepository2 = repositoryFactory.getMapper(ComplexMappedRepository2.class);
        final List<CreatorComplexLolInfo> result = complexMappedRepository2.getAllComplexLolInfo();
        Assert.assertEquals(result.size(), 2);
        {
            final CreatorComplexLolInfo first = result.get(0);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("abc", first.getName());
            Assert.assertNotNull(first.getExtraInfo());
            Assert.assertEquals("extra1", first.getExtraInfo().extraField);
        }
        //
        {
            final CreatorComplexLolInfo second = result.get(1);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("def", second.getName());
            Assert.assertNotNull(second.getExtraInfo());
            Assert.assertEquals("extra2", second.getExtraInfo().extraField);
        }
    }
}
