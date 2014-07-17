package ru.shadam.ftlmapper;

import entity.inherit.SlaveWithAddressCreator;
import entity.inherit.SlaveWithAddressField;
import entity.inherit.SlaveWithAddressProperty;
import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.List;

/**
 * @author sala
 */
public class InheritanceMappedTypeTest extends BaseTest {

    public static final String SELECT = "select id, name, streetName as address_streetName, houseNumber as address_houseNumber from person";

    private static interface Repository1 {
        @Query(SELECT)
        public List<SlaveWithAddressCreator> getAll();
    }

    private static interface Repository2 {
        @Query(SELECT)
        public List<SlaveWithAddressField> getAll();
    }

    private static interface Repository3 {
        @Query(SELECT)
        public List<SlaveWithAddressProperty> getAll();
    }

    @Test
    public void test1() {
        final Repository1 repository2 = repositoryFactory.getMapper(Repository1.class);
        final List<SlaveWithAddressCreator> all = repository2.getAll();
        Assert.assertEquals(2, all.size());
        {
            final SlaveWithAddressCreator first = all.get(0);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("John Doe", first.getName());
            Assert.assertEquals("Lenin street", first.getAddress().streetName);
            Assert.assertEquals("1A", first.getAddress().houseNumber);
        }
        //
        {
            final SlaveWithAddressCreator second = all.get(1);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("Jane Doe", second.getName());
            Assert.assertEquals("Trozki street", second.getAddress().streetName);
            Assert.assertEquals("2B", second.getAddress().houseNumber);
        }
    }

    @Test
    public void test2() {
        final Repository2 repository2 = repositoryFactory.getMapper(Repository2.class);
        final List<SlaveWithAddressField> all = repository2.getAll();
        Assert.assertEquals(2, all.size());
        {
            final SlaveWithAddressField first = all.get(0);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("John Doe", first.getName());
            Assert.assertEquals("Lenin street", first.address.streetName);
            Assert.assertEquals("1A", first.address.houseNumber);
        }
        //
        {
            final SlaveWithAddressField second = all.get(1);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("Jane Doe", second.getName());
            Assert.assertEquals("Trozki street", second.address.streetName);
            Assert.assertEquals("2B", second.address.houseNumber);
        }
    }

    @Test
    public void test3() {
        final Repository3 repository2 = repositoryFactory.getMapper(Repository3.class);
        final List<SlaveWithAddressProperty> all = repository2.getAll();
        Assert.assertEquals(2, all.size());
        {
            final SlaveWithAddressProperty first = all.get(0);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("John Doe", first.getName());
            Assert.assertEquals("Lenin street", first.getAddress().streetName);
            Assert.assertEquals("1A", first.getAddress().houseNumber);
        }
        //
        {
            final SlaveWithAddressProperty second = all.get(1);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("Jane Doe", second.getName());
            Assert.assertEquals("Trozki street", second.getAddress().streetName);
            Assert.assertEquals("2B", second.getAddress().houseNumber);
        }
    }
}
