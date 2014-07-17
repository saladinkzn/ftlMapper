package ru.shadam.ftlmapper;

import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;
import ru.shadam.ftlmapper.entity.ConstructorExtraLolInfo;

import java.util.List;

/**
 * @author sala
 */
public class InheritanceMappedTypeTest extends BaseTest {
    private static interface Repository2 {
        @Query("select l.id as id, l.name as name, le.extra_field as extraInfo from lol l join lol_extra le on l.id = le.id")
        public List<ConstructorExtraLolInfo> getAll();
    }

    @Test
    public void test2() {
        final Repository2 repository2 = repositoryFactory.getMapper(Repository2.class);
        final List<ConstructorExtraLolInfo> all = repository2.getAll();
        Assert.assertEquals(2, all.size());
        final ConstructorExtraLolInfo first = all.get(0);
        Assert.assertEquals(1L, first.getId());
        Assert.assertEquals("abc", first.getName());
        Assert.assertEquals("extra1", first.getExtraInfo());
        //
        final ConstructorExtraLolInfo second = all.get(1);
        Assert.assertEquals(2L, second.getId());
        Assert.assertEquals("def", second.getName());
        Assert.assertEquals("extra2", second.getExtraInfo());
    }
}
