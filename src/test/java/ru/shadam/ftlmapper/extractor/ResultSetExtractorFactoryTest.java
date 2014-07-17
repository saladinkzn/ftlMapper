package ru.shadam.ftlmapper.extractor;

import entity.Address;
import entity.Master;
import entity.Person;
import entity.Slave;
import entity.property.Entity;
import org.junit.Test;
import org.mockito.Mockito;
import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTList;
import ru.shadam.ftlmapper.ast.domain.ASTObject;
import ru.shadam.ftlmapper.ast.domain.ASTPrimitive;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.CollectionResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.object.ConstructorInstanceProvider;
import ru.shadam.ftlmapper.extractor.inner.object.InstanceProvider;
import ru.shadam.ftlmapper.extractor.inner.object.ObjectResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.ListResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.CheckAttributesPredicate;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.ResultSetPredicate;
import ru.shadam.ftlmapper.extractor.inner.simple.index.LongColumnIndexResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.simple.index.SimpleColumnIndexResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.simple.name.LongColumnNameResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.simple.name.SimpleColumnNameResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.simple.name.StringColumnNameResultSetExtractor;
import ru.shadam.ftlmapper.extractor.state.SimpleState;
import util.ReflectionUtil;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ResultSetExtractorFactoryTest {

    @Test
    public void testGenerate() throws Exception {
        final ResultSetExtractorFactory resultSetExtractorFactory = new ResultSetExtractorFactory();
        final ASTBase ast = new ASTObject(
                "",
                "",
                Person.class,
                Arrays.asList(
                        new ASTPrimitive("id", "id", long.class),
                        new ASTPrimitive("name", "name", String.class),
                        new ASTObject(
                                "address",
                                "address",
                                Address.class,
                                Arrays.<ASTBase>asList(
                                        new ASTPrimitive("streetName", "streetName", String.class),
                                        new ASTPrimitive("houseNumber", "houseNumber", String.class)
                                ),
                                null
                        )
                ),
                null
        );
        final ResultSetExtractor<?> generate = resultSetExtractorFactory.generate(ast);
        assertTrue(generate instanceof RootResultSetExtractor<?>);
        final RootResultSetExtractor<?> rootResultSetExtractor = (RootResultSetExtractor) generate;
        assertNotNull(rootResultSetExtractor);
        final InnerResultSetExtractor<?> innerResultSetExtractor =
                ReflectionUtil.getFieldValue(
                        rootResultSetExtractor, "innerResultSetExtractor", RootResultSetExtractor.class
                );
        assertTrue(innerResultSetExtractor instanceof ObjectResultSetExtractor);
        final ObjectResultSetExtractor<?> objectResultSetExtractor = (ObjectResultSetExtractor<?>) innerResultSetExtractor;

//        final Class<?> clazz = ReflectionUtil.getFieldValue(objectResultSetExtractor, "clazz", ObjectResultSetExtractor.class);
//        assertEquals(Person.class, clazz);
        final Map<String, InnerResultSetExtractor<? extends SimpleState<?>>> attributeExtractors =
                ReflectionUtil.getFieldValue(
                        objectResultSetExtractor, "attributeExtractors", ObjectResultSetExtractor.class
                );
        assertNotNull(attributeExtractors);
        assertEquals(3, attributeExtractors.size());
        assertTrue(attributeExtractors.containsKey("id"));
        assertTrue(attributeExtractors.containsKey("name"));
        assertTrue(attributeExtractors.containsKey("address"));
        {
            InnerResultSetExtractor<?> idExtractor = attributeExtractors.get("id");
            assertTrue(idExtractor instanceof LongColumnNameResultSetExtractor);
            final LongColumnNameResultSetExtractor longResultSetExtractor = (LongColumnNameResultSetExtractor) idExtractor;
            final String columnName = ReflectionUtil.getFieldValue(longResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
            assertEquals("id", columnName);
        }
        {
            InnerResultSetExtractor<?> nameExtractor = attributeExtractors.get("name");
            assertTrue(nameExtractor instanceof StringColumnNameResultSetExtractor);
            final StringColumnNameResultSetExtractor stringResultSetExtractor = (StringColumnNameResultSetExtractor) nameExtractor;
            final String columnName = ReflectionUtil.getFieldValue(stringResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
            assertEquals("name", columnName);
        }
        {
            InnerResultSetExtractor<?> addressExtractor = attributeExtractors.get("address");
            assertTrue(addressExtractor instanceof ObjectResultSetExtractor);
            final ObjectResultSetExtractor objectResultSetExtractor2 = (ObjectResultSetExtractor) addressExtractor;
//            final Class<?> clazz2 = ReflectionUtil.getFieldValue(objectResultSetExtractor2, "clazz", ObjectResultSetExtractor.class);
//            assertEquals(Address.class, clazz2);
            //
            final Map<String, InnerResultSetExtractor<?>> fieldExtractors = ReflectionUtil.getFieldValue(objectResultSetExtractor2, "attributeExtractors", ObjectResultSetExtractor.class);
            assertEquals(2, fieldExtractors.size());
            assertTrue(fieldExtractors.containsKey("streetName"));
            assertTrue(fieldExtractors.containsKey("houseNumber"));
            //
            {
                final InnerResultSetExtractor<?> streetNameExtractor = fieldExtractors.get("streetName");
                assertTrue(streetNameExtractor instanceof StringColumnNameResultSetExtractor);
                final StringColumnNameResultSetExtractor streetNameStringResultSetExtractor = ((StringColumnNameResultSetExtractor) streetNameExtractor);
                //
                final String columnName = ReflectionUtil.getFieldValue(streetNameStringResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                assertEquals("address_streetName", columnName);
            }
            {
                final InnerResultSetExtractor<?> streetNameExtractor = fieldExtractors.get("houseNumber");
                assertTrue(streetNameExtractor instanceof StringColumnNameResultSetExtractor);
                final StringColumnNameResultSetExtractor streetNameStringResultSetExtractor = ((StringColumnNameResultSetExtractor) streetNameExtractor);
                //
                final String columnName = ReflectionUtil.getFieldValue(streetNameStringResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                assertEquals("address_houseNumber", columnName);
            }
        }

    }

    @Test
    public void testGenerate2() throws Exception {
        final ResultSetExtractorFactory resultSetExtractorFactory = new ResultSetExtractorFactory();
        final ASTBase ast = new ASTObject(
                "", "",
                Master.class,
                Arrays.<ASTBase>asList(
                        new ASTPrimitive("id", "id", long.class),
                        new ASTPrimitive("name", "name", String.class)
                ),
                new ASTList(
                        "slaves", "slaves",
                        new ASTObject(
                            "slaves", "slaves",
                            Slave.class,
                            Arrays.<ASTBase>asList(
                                    new ASTPrimitive("id", "id", long.class),
                                    new ASTPrimitive("name", "name", String.class)
                            ),
                            null
                        ))
        );
        final ResultSetExtractor<?> resultSetExtractor = resultSetExtractorFactory.generate(ast);
        assertNotNull(resultSetExtractor);
        assertTrue(resultSetExtractor instanceof RootResultSetExtractor);
        final RootResultSetExtractor rootResultSetExtractor = ((RootResultSetExtractor) resultSetExtractor);
        //
        final InnerResultSetExtractor innerResultSetExtractor = ReflectionUtil.getFieldValue(rootResultSetExtractor, "innerResultSetExtractor", RootResultSetExtractor.class);
        assertNotNull(innerResultSetExtractor);
        assertTrue(innerResultSetExtractor instanceof ObjectResultSetExtractor);
        {
            final ObjectResultSetExtractor innerObjectResultSetExtractor = ((ObjectResultSetExtractor) innerResultSetExtractor);
            //
            final InstanceProvider<?> instanceProvider = ReflectionUtil.getFieldValue(innerObjectResultSetExtractor, "instanceProvider", ObjectResultSetExtractor.class);
            assertTrue(instanceProvider instanceof ConstructorInstanceProvider<?>);
            final ConstructorInstanceProvider<?> constructorInstanceProvider = ((ConstructorInstanceProvider) instanceProvider);
            assertEquals(
                    util.ReflectionUtil.getDefaultConstructor(Master.class),
                    ReflectionUtil.getFieldValue(constructorInstanceProvider, "constructor", ConstructorInstanceProvider.class)
            );
            //
            final Map<String, InnerResultSetExtractor<?>> attributeExtractors = ReflectionUtil.getFieldValue(innerObjectResultSetExtractor, "attributeExtractors", ObjectResultSetExtractor.class);
            assertNotNull(attributeExtractors);
            assertEquals(3, attributeExtractors.size());
            assertTrue(attributeExtractors.containsKey("id"));
            assertTrue(attributeExtractors.containsKey("name"));
            assertTrue(attributeExtractors.containsKey("slaves"));
            {
                final LongColumnNameResultSetExtractor longResultSetExtractor = (LongColumnNameResultSetExtractor) attributeExtractors.get("id");
                final String longResultSetExtractorColumnName = ReflectionUtil.getFieldValue(longResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                assertEquals("id", longResultSetExtractorColumnName);
            }
            {
                final StringColumnNameResultSetExtractor longResultSetExtractor = (StringColumnNameResultSetExtractor) attributeExtractors.get("name");
                final String longResultSetExtractorColumnName = ReflectionUtil.getFieldValue(longResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                assertEquals("name", longResultSetExtractorColumnName);
            }
            {
                final ListResultSetExtractor listResultSetExtractor = (ListResultSetExtractor) attributeExtractors.get("slaves");
                //
                final InnerResultSetExtractor innerResultSetExtractor2 = ReflectionUtil.getFieldValue(listResultSetExtractor, "innerResultSetExtractor", CollectionResultSetExtractor.class);
                //
                {
                    assertTrue(innerResultSetExtractor2 instanceof ObjectResultSetExtractor);
                    final ObjectResultSetExtractor innerObjectResultSetExtractor2 = ((ObjectResultSetExtractor) innerResultSetExtractor2);
                    //
                    final InstanceProvider<?> innerInstanceProvider = ReflectionUtil.getFieldValue(innerObjectResultSetExtractor2, "instanceProvider", ObjectResultSetExtractor.class);
                    assertTrue(innerInstanceProvider instanceof ConstructorInstanceProvider<?>);
                    final ConstructorInstanceProvider<?> defaultConstructorInnerInstanceProvider = ((ConstructorInstanceProvider) innerInstanceProvider);
                    assertEquals(
                            util.ReflectionUtil.getDefaultConstructor(Slave.class),
                            ReflectionUtil.getFieldValue(defaultConstructorInnerInstanceProvider, "constructor", ConstructorInstanceProvider.class)
                    );
                    // TODO: check fields
                    //
                    final Map<String, InnerResultSetExtractor> slaveAttributeExtractors = ReflectionUtil.getFieldValue(innerObjectResultSetExtractor2, "attributeExtractors", ObjectResultSetExtractor.class);
                    assertNotNull(slaveAttributeExtractors);
                    assertEquals(2, slaveAttributeExtractors.size());
                    assertTrue(slaveAttributeExtractors.containsKey("id"));
                    assertTrue(slaveAttributeExtractors.containsKey("name"));
                    //
                    {
                        final LongColumnNameResultSetExtractor longResultSetExtractor = (LongColumnNameResultSetExtractor) slaveAttributeExtractors.get("id");
                        final String columnName = ReflectionUtil.getFieldValue(longResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                        assertEquals("slaves_id", columnName);
                    }
                    {
                        final StringColumnNameResultSetExtractor stringResultSetExtractor = (StringColumnNameResultSetExtractor) slaveAttributeExtractors.get("name");
                        final String columnName = ReflectionUtil.getFieldValue(stringResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                        assertEquals("slaves_name", columnName);
                    }
                }
                //
                final ResultSetPredicate resultSetPredicate = ReflectionUtil.getFieldValue(listResultSetExtractor, "listCompletePredicate", CollectionResultSetExtractor.class);
                assertTrue(resultSetPredicate instanceof CheckAttributesPredicate);
                final CheckAttributesPredicate checkAttributesPredicate = ((CheckAttributesPredicate) resultSetPredicate);
                final List<InnerResultSetExtractor> parentAttributeExtractors = ReflectionUtil.getFieldValue(checkAttributesPredicate, "extractors", CheckAttributesPredicate.class);
                assertNotNull(parentAttributeExtractors);
                assertEquals(2, parentAttributeExtractors.size());
                {
                    LongColumnNameResultSetExtractor longResultSetExtractor = (LongColumnNameResultSetExtractor) parentAttributeExtractors.get(0);
                    assertNotNull(longResultSetExtractor);
                    final String columnName = ReflectionUtil.getFieldValue(longResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                    assertEquals("id", columnName);
                }
                {
                    StringColumnNameResultSetExtractor stringResultSetExtractor = (StringColumnNameResultSetExtractor) parentAttributeExtractors.get(1);
                    assertNotNull(stringResultSetExtractor);
                    final String columnName = ReflectionUtil.getFieldValue(stringResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class);
                    assertEquals("name", columnName);
                }
            }
        }
    }

    @Test
    public void test3() throws Exception {
        final ResultSetExtractorFactory resultSetExtractorFactory = new ResultSetExtractorFactory();
        final ASTBase ast = new ASTList(
                "", "",
                new ASTPrimitive("", "", long.class));
        final ResultSetExtractor<?> resultSetExtractor = resultSetExtractorFactory.generate(ast);
        assertNotNull(resultSetExtractor);
        assertTrue(resultSetExtractor instanceof RootResultSetExtractor);
        final RootResultSetExtractor rootResultSetExtractor = ((RootResultSetExtractor) resultSetExtractor);
        //
        final InnerResultSetExtractor innerResultSetExtractor = ReflectionUtil.getFieldValue(rootResultSetExtractor, "innerResultSetExtractor", RootResultSetExtractor.class);
        assertTrue(innerResultSetExtractor instanceof ListResultSetExtractor);
        final ListResultSetExtractor innerResultSetListResultSetExtractor = ((ListResultSetExtractor) innerResultSetExtractor);
        //
        {
            final InnerResultSetExtractor listInnerResultSetExtract = ReflectionUtil.getFieldValue(innerResultSetListResultSetExtractor, "innerResultSetExtractor", CollectionResultSetExtractor.class);
            assertTrue(listInnerResultSetExtract instanceof LongColumnIndexResultSetExtractor);
            //
            final LongColumnIndexResultSetExtractor listInnerResultSetExtractor2 = ((LongColumnIndexResultSetExtractor) listInnerResultSetExtract);
            final int columnIndex = ReflectionUtil.getFieldValue(listInnerResultSetExtractor2, "columnIndex", SimpleColumnIndexResultSetExtractor.class);
            assertEquals(1, columnIndex);
        }
    }

    @Test
    public void test4() throws Exception {
        final ResultSetExtractorFactory resultSetExtractorFactory = new ResultSetExtractorFactory();
        final ASTBase ast = new ASTObject(
            "", "",
            Entity.class,
            Arrays.<ASTBase>asList(
                    new ASTPrimitive("entity_id", "id", long.class),
                    new ASTPrimitive("entity_name", "name", String.class)
            ),
            null
        );
        final ResultSetExtractor<?> resultSetExtractor = resultSetExtractorFactory.generate(ast);
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("entity_id")).thenReturn(1L);
        Mockito.when(resultSet.getString("entity_name")).thenReturn("abc");
        //
        final Object result = resultSetExtractor.extractResult(resultSet);
        assertNotNull(result);
        assertTrue(result instanceof Entity);
        final Entity entity = (Entity) result;
        assertEquals(1L, entity.id);
        assertEquals("abc", entity.getName());
    }
}