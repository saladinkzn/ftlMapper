package ru.shadam.ftlmapper.extractor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import entity.Address;
import entity.Master;
import entity.Person;
import entity.Slave;
import entity.property.Master2;
import entity.property.Slave2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.ListResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.CheckAttributesPredicate;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.Predicates;
import ru.shadam.ftlmapper.extractor.inner.object.*;
import ru.shadam.ftlmapper.extractor.inner.simple.name.LongColumnNameResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.simple.name.StringColumnNameResultSetExtractor;
import ru.shadam.ftlmapper.extractor.state.SimpleState;
import util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ResultSetExtractorTest {
    private EmbeddedDatabase embeddedDatabase;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addDefaultScripts()
                .build();
    }

    @Test
    public void test() throws SQLException {
        final ResultSetExtractor<?> resultSetExtractor = new RootResultSetExtractor<>(
                new ListResultSetExtractor<>(
                        new ObjectResultSetExtractor<>(
                                new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Master.class), new ArrayList<String>()),
                                Lists.newArrayList(
                                        new FieldInstanceFiller<Master>(util.ReflectionUtil.getField(Master.class, "id"), "id"),
                                        new FieldInstanceFiller<Master>(util.ReflectionUtil.getField(Master.class, "name"), "name"),
                                        new FieldInstanceFiller<Master>(util.ReflectionUtil.getField(Master.class, "slaves"), "slaves")
                                ),
                                ImmutableMap.of(
                                        "id", new LongColumnNameResultSetExtractor("id"),
                                        "name", new StringColumnNameResultSetExtractor("name"),
                                        "slaves", new ListResultSetExtractor<>(
                                                new ObjectResultSetExtractor<>(
                                                        new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Slave.class), new ArrayList<String>()),
                                                        Lists.newArrayList(
                                                                new FieldInstanceFiller<Slave>(util.ReflectionUtil.getField(Slave.class, "id"), "id"),
                                                                new FieldInstanceFiller<Slave>(util.ReflectionUtil.getField(Slave.class, "name"), "name")
                                                        ),
                                                        ImmutableMap.<String, InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                                                "id", new LongColumnNameResultSetExtractor("slave_id"),
                                                                "name", new StringColumnNameResultSetExtractor("slave_name")
                                                        )
                                                ),
                                                new CheckAttributesPredicate(
                                                        ImmutableList.<InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                                                new LongColumnNameResultSetExtractor("id"),
                                                                new StringColumnNameResultSetExtractor("name")
                                                        )
                                                )
                                        )
                                )
                        ),
                        Predicates.alwaysFalse()
                )
        );
        //
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select m.id, m.name, s.id as slave_id, s.name as slave_name " +
                                "from Master m " +
                                " join Slave s on m.id = s.master_id"
                )) {
                    final Object result = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(result);
                    Assert.assertTrue(result instanceof List);
                    final List<Master> masters = (List<Master>) result;
                    Assert.assertEquals(2, masters.size());
                    {
                        final Master master = masters.get(0);
                        Assert.assertEquals(1l, master.id);
                        Assert.assertEquals("abc", master.name);
                        Assert.assertNotNull(master.slaves);
                        final List<Slave> slaves = master.slaves;
                        Assert.assertEquals(2, slaves.size());
                        {
                            final Slave slave = slaves.get(0);
                            Assert.assertEquals(1L, slave.id);
                            Assert.assertEquals("abc_slave_1", slave.name);
                        }
                        {
                            final Slave slave = slaves.get(1);
                            Assert.assertEquals(2L, slave.id);
                            Assert.assertEquals("abc_slave_2", slave.name);
                        }
                    }
                    {
                        final Master master = masters.get(1);
                        Assert.assertEquals(2L, master.id);
                        Assert.assertEquals("def", master.name);
                        final List<Slave> slaves = master.slaves;
                        Assert.assertNotNull(slaves);
                        Assert.assertEquals(2, slaves.size());
                        {
                            final Slave slave = slaves.get(0);
                            Assert.assertEquals(3L, slave.id);
                            Assert.assertEquals("def_slave_1", slave.name);
                        }
                        {
                            final Slave slave = slaves.get(1);
                            Assert.assertEquals(4L, slave.id);
                            Assert.assertEquals("def_slave_2", slave.name);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void test_2() throws SQLException {
        final ResultSetExtractor<?> resultSetExtractor = new RootResultSetExtractor<>(
                new ListResultSetExtractor<>(
                        new ObjectResultSetExtractor<>(
                                new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Master2.class), new ArrayList<String>()),
                                Lists.newArrayList(
                                        new SetterInstanceFiller<Master2>(util.ReflectionUtil.getSetter(Master2.class, "id"), "id"),
                                        new SetterInstanceFiller<Master2>(util.ReflectionUtil.getSetter(Master2.class, "name"), "name"),
                                        new SetterInstanceFiller<Master2>(util.ReflectionUtil.getSetter(Master2.class, "slaves"), "slaves")
                                ),
                                ImmutableMap.of(
                                        "id", new LongColumnNameResultSetExtractor("id"),
                                        "name", new StringColumnNameResultSetExtractor("name"),
                                        "slaves", new ListResultSetExtractor<>(
                                                new ObjectResultSetExtractor<>(
                                                        new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Slave2.class), new ArrayList<String>()),
                                                        Lists.newArrayList(
                                                                new SetterInstanceFiller<Slave2>(util.ReflectionUtil.getSetter(Slave2.class, "id"), "id"),
                                                                new SetterInstanceFiller<Slave2>(util.ReflectionUtil.getSetter(Slave2.class, "name"), "name")
                                                        ),
                                                        ImmutableMap.<String, InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                                                "id", new LongColumnNameResultSetExtractor("slave_id"),
                                                                "name", new StringColumnNameResultSetExtractor("slave_name")
                                                        )
                                                ),
                                                new CheckAttributesPredicate(
                                                        ImmutableList.<InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                                                new LongColumnNameResultSetExtractor("id"),
                                                                new StringColumnNameResultSetExtractor("name")
                                                        )
                                                )
                                        )
                                )
                        ),
                        Predicates.alwaysFalse()
                )
        );
        //
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select m.id, m.name, s.id as slave_id, s.name as slave_name " +
                                "from Master m " +
                                " join Slave s on m.id = s.master_id"
                )) {
                    final Object result = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(result);
                    Assert.assertTrue(result instanceof List);
                    final List<Master2> masters = (List<Master2>) result;
                    Assert.assertEquals(2, masters.size());
                    {
                        final Master2 master = masters.get(0);
                        Assert.assertEquals(1l, master.getId());
                        Assert.assertEquals("abc", master.getName());
                        Assert.assertNotNull(master.getSlaves());
                        final List<Slave2> slaves = master.getSlaves();
                        Assert.assertEquals(2, slaves.size());
                        {
                            final Slave2 slave = slaves.get(0);
                            Assert.assertEquals(1L, slave.getId());
                            Assert.assertEquals("abc_slave_1", slave.getName());
                        }
                        {
                            final Slave2 slave = slaves.get(1);
                            Assert.assertEquals(2L, slave.getId());
                            Assert.assertEquals("abc_slave_2", slave.getName());
                        }
                    }
                    {
                        final Master2 master = masters.get(1);
                        Assert.assertEquals(2L, master.getId());
                        Assert.assertEquals("def", master.getName());
                        final List<Slave2> slaves = master.getSlaves();
                        Assert.assertNotNull(slaves);
                        Assert.assertEquals(2, slaves.size());
                        {
                            final Slave2 slave = slaves.get(0);
                            Assert.assertEquals(3L, slave.getId());
                            Assert.assertEquals("def_slave_1", slave.getName());
                        }
                        {
                            final Slave2 slave = slaves.get(1);
                            Assert.assertEquals(4L, slave.getId());
                            Assert.assertEquals("def_slave_2", slave.getName());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void test2() throws SQLException {
        final ResultSetExtractor<Slave> resultSetExtractor =
                new RootResultSetExtractor<>(
                        new ObjectResultSetExtractor<>(
                                new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Slave.class), new ArrayList<String>()),
                                Lists.newArrayList(
                                        new FieldInstanceFiller<Slave>(util.ReflectionUtil.getField(Slave.class, "id"), "id"),
                                        new FieldInstanceFiller<Slave>(util.ReflectionUtil.getField(Slave.class, "name"), "name")
                                ),
                                ImmutableMap.<String, InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                        "id", new LongColumnNameResultSetExtractor("id"),
                                        "name", new StringColumnNameResultSetExtractor("name")
                                )
                        )
                );
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select id, name from Slave where id = 1"
                )) {
                    final Slave slave = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(slave);
                    Assert.assertEquals(1L, slave.id);
                    Assert.assertEquals("abc_slave_1", slave.name);
                }
            }
        }
    }

    @Test
    public void test3() throws SQLException {
        final ResultSetExtractor<List<Slave>> resultSetExtractor =
                new RootResultSetExtractor<>(
                        new ListResultSetExtractor<>(
                                new ObjectResultSetExtractor<>(
                                        new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Slave.class), new ArrayList<String>()),
                                        Lists.newArrayList(
                                                new FieldInstanceFiller<Slave>(util.ReflectionUtil.getField(Slave.class, "id"), "id"),
                                                new FieldInstanceFiller<Slave>(util.ReflectionUtil.getField(Slave.class, "name"), "name")
                                        ),
                                        ImmutableMap.<String, InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                                "id", new LongColumnNameResultSetExtractor("id"),
                                                "name", new StringColumnNameResultSetExtractor("name")
                                        )
                                ), Predicates.alwaysFalse()
                        )
                );
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select id, name from Slave"
                )) {
                    final List<Slave> slaves = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(slaves);
                    {
                        final Slave slave = slaves.get(0);
                        Assert.assertEquals(1L, slave.id);
                        Assert.assertEquals("abc_slave_1", slave.name);
                    }
                    {
                        final Slave slave = slaves.get(1);
                        Assert.assertEquals(2L, slave.id);
                        Assert.assertEquals("abc_slave_2", slave.name);
                    }
                    {
                        final Slave slave = slaves.get(2);
                        Assert.assertEquals(3L, slave.id);
                        Assert.assertEquals("def_slave_1", slave.name);
                    }
                    {
                        final Slave slave = slaves.get(3);
                        Assert.assertEquals(4L, slave.id);
                        Assert.assertEquals("def_slave_2", slave.name);
                    }
                }
            }
        }
    }

    @Test
    public void test4() throws SQLException {
        final ResultSetExtractor<List<Long>> resultSetExtractor =
                new RootResultSetExtractor<>(
                        new ListResultSetExtractor<>(
                                new LongColumnNameResultSetExtractor("id"), Predicates.alwaysFalse()
                        )
                );
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select id, name from Slave"
                )) {
                    final List<Long> slaveIds = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(slaveIds);
                    Assert.assertEquals(1L, (long) slaveIds.get(0));
                    Assert.assertEquals(2L, (long) slaveIds.get(1));
                    Assert.assertEquals(3L, (long) slaveIds.get(2));
                    Assert.assertEquals(4L, (long) slaveIds.get(3));
                }
            }
        }
    }

    @Test
    public void personTest() throws SQLException {
        final ObjectResultSetExtractor<Person> personObjectResultSetExtractor = new ObjectResultSetExtractor<>(
                new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Person.class), new ArrayList<String>()),
                Lists.newArrayList(
                        new FieldInstanceFiller<Person>(util.ReflectionUtil.getField(Person.class, "id"), "id"),
                        new FieldInstanceFiller<Person>(util.ReflectionUtil.getField(Person.class, "name"), "name"),
                        new FieldInstanceFiller<Person>(util.ReflectionUtil.getField(Person.class, "address"), "address")
                ),
                ImmutableMap.of(
                        "id", new LongColumnNameResultSetExtractor("id"),
                        "name", new StringColumnNameResultSetExtractor("name"),
                        "address", new ObjectResultSetExtractor<>(
                                new ConstructorInstanceProvider<>(util.ReflectionUtil.getDefaultConstructor(Address.class), new ArrayList<String>()),
                                Lists.newArrayList(
                                        new FieldInstanceFiller<Address>(util.ReflectionUtil.getField(Address.class, "streetName"), "streetName"),
                                        new FieldInstanceFiller<Address>(util.ReflectionUtil.getField(Address.class, "houseNumber"), "houseNumber")
                                ),
                                ImmutableMap.<String, InnerResultSetExtractor<? extends SimpleState<?>>>of(
                                        "streetName", new StringColumnNameResultSetExtractor("streetName"),
                                        "houseNumber", new StringColumnNameResultSetExtractor("houseNumber")
                                )
                        )
                )
        );
        ResultSetExtractor<List<Person>> resultSetExtractor = new RootResultSetExtractor<>(
                new ListResultSetExtractor<>(
                        personObjectResultSetExtractor, Predicates.alwaysFalse()
                )
        );
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select id, name, streetName, houseNumber" +
                                " from Person"
                )) {
                    List<Person> persons = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(persons);
                    Assert.assertEquals(2, persons.size());
                    {
                        final Person person = persons.get(0);
                        Assert.assertNotNull(person);
                        Assert.assertEquals(1L, person.id);
                        Assert.assertEquals("John Doe", person.name);
                        final Address address = person.address;
                        Assert.assertNotNull(address);
                        Assert.assertEquals("Lenin street", address.streetName);
                        Assert.assertEquals("1A", address.houseNumber);
                    }
                    {
                        final Person person = persons.get(1);
                        Assert.assertNotNull(person);
                        Assert.assertEquals(2L, person.id);
                        Assert.assertEquals("Jane Doe", person.name);
                        final Address address = person.address;
                        Assert.assertNotNull(address);
                        Assert.assertEquals("Trozki street", address.streetName);
                        Assert.assertEquals("2B", address.houseNumber);
                    }
                }
            }
        }
    }

    @Test
    public void creatorTest() throws SQLException {
        final Constructor<entity.creator.Master> creatorConstructor = ReflectionUtil.getCreatorConstructor(entity.creator.Master.class);
        final List<String> getNames = ReflectionUtil.getNames(creatorConstructor);
        final ObjectResultSetExtractor<entity.creator.Master> personObjectResultSetExtractor = new ObjectResultSetExtractor<>(
                new ConstructorInstanceProvider<>(creatorConstructor, getNames),
                Lists.<InstanceFiller<entity.creator.Master>>newArrayList(
                ),
                ImmutableMap.<String, InnerResultSetExtractor<?>>of(
                        "id", new LongColumnNameResultSetExtractor("id"),
                        "name", new StringColumnNameResultSetExtractor("name")
                )
        );
        ResultSetExtractor<List<entity.creator.Master>> resultSetExtractor = new RootResultSetExtractor<>(
                new ListResultSetExtractor<>(
                        personObjectResultSetExtractor, Predicates.alwaysFalse()
                )
        );
        try(Connection connection = embeddedDatabase.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(
                        "select id, name from Master"
                )) {
                    List<entity.creator.Master> persons = resultSetExtractor.extractResult(resultSet);
                    Assert.assertNotNull(persons);
                    Assert.assertEquals(2, persons.size());
                    {
                        final entity.creator.Master person = persons.get(0);
                        Assert.assertNotNull(person);
                        Assert.assertEquals(1L, person.getId());
                        Assert.assertEquals("abc", person.getName());
                    }
                    {
                        final entity.creator.Master person = persons.get(1);
                        Assert.assertNotNull(person);
                        Assert.assertEquals(2L, person.getId());
                        Assert.assertEquals("def", person.getName());
                    }
                }
            }
        }
    }

    @After
    public void shutdown() {
        embeddedDatabase.shutdown();
    }
}
