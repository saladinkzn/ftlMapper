package ru.shadam.ftlmapper;

import entity.Person;
import org.junit.Assert;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ComplexMappedTypeTest extends BaseTest {
    public static interface ComplexMappedRepository {
        // Haha I'm too lazy to create a new table ;)
        @Query("select id, name, streetName as address_streetName, houseNumber as address_houseNumber from person")
        List<Person> getAllComplexLolInfo();
    }

    public static interface ComplexMappedRepository2 {
        // Haha I'm too lazy to create a new table ;)
        @Query("select id, name, streetName as address_streetName, houseNumber as address_houseNumber from person")
        List<entity.creator.Person> getAllComplexLolInfo2();
    }

    //
    // (1, 'John Doe', 'Lenin street', '1A'),
    // (2, 'Jane Doe', 'Trozki street', '2B');
    //

    @Test
    public void testComplexTest() {
        final ComplexMappedRepository complexMappedRepository = repositoryFactory.getMapper(ComplexMappedRepository.class);
        final List<Person> result = complexMappedRepository.getAllComplexLolInfo();
        Assert.assertEquals(result.size(), 2);
        final Person first = result.get(0);
        Assert.assertEquals(1L, first.id);
        Assert.assertEquals("John Doe", first.name);
        Assert.assertNotNull(first.address);
        Assert.assertEquals("Lenin street", first.address.streetName);
        Assert.assertEquals("1A", first.address.houseNumber);
        //
        final Person second = result.get(1);
        Assert.assertEquals(2L, second.id);
        Assert.assertEquals("Trozki street", second.address.streetName);
        Assert.assertEquals("2B", second.address.houseNumber);
        System.out.println(result);
    }

    @Test
    public void testComplexTest2() {
        final ComplexMappedRepository2 complexMappedRepository2 = repositoryFactory.getMapper(ComplexMappedRepository2.class);
        final List<entity.creator.Person> result = complexMappedRepository2.getAllComplexLolInfo2();
        {
            Assert.assertEquals(result.size(), 2);
            final entity.creator.Person first = result.get(0);
            Assert.assertEquals(1L, first.getId());
            Assert.assertEquals("John Doe", first.getName());
            Assert.assertNotNull(first.getAddress());
            Assert.assertEquals("Lenin street", first.getAddress().streetName);
            Assert.assertEquals("1A", first.getAddress().houseNumber);
        }
        //
        {
            final entity.creator.Person second = result.get(1);
            Assert.assertEquals(2L, second.getId());
            Assert.assertEquals("Trozki street", second.getAddress().streetName);
            Assert.assertEquals("2B", second.getAddress().houseNumber);
            System.out.println(result);
        }
    }
}
