package ru.shadam.ftlmapper.ast;

import com.google.common.reflect.TypeToken;
import entity.Master;
import entity.Slave;
import entity.property.Entity;
import org.junit.Test;
import ru.shadam.ftlmapper.annotations.query.KeyExtractor;
import ru.shadam.ftlmapper.ast.domain.*;
import ru.shadam.ftlmapper.ast.module.ParsingContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Timur Shakurov
 */
public class ASTParserTest {

    @Test
    public void test() {
        final ASTParser astParser = new ASTParser();
        final ASTBase astBase = astParser.parse(new ParsingContext("", ""), Master.class);
        assertTrue(astBase instanceof ASTObject);
        final ASTObject astObject = (ASTObject) astBase;
        assertEquals(Master.class, astObject.getClazz());
        assertEquals("", astObject.getGetName());
        assertEquals("", astObject.getSetName());
        //
        final List<ASTBase> astObjectAttributes = astObject.getAttributes();
        assertNotNull(astObjectAttributes);
        assertEquals(2, astObjectAttributes.size());
        //
        {
            final ASTBase firstAttribute = astObjectAttributes.get(0);
            assertTrue(firstAttribute instanceof ASTPrimitive);
            assertEquals(long.class, firstAttribute.getClazz());
            assertEquals("id", firstAttribute.getGetName());
            assertEquals("id", firstAttribute.getSetName());
        }
        //
        {
            final ASTBase secondAttribute = astObjectAttributes.get(1);
            assertTrue(secondAttribute instanceof ASTPrimitive);
            assertEquals(String.class, secondAttribute.getClazz());
            assertEquals("name", secondAttribute.getGetName());
            assertEquals("name", secondAttribute.getSetName());
        }
        //
        assertTrue(astObject.hasInnerObject());
        final ASTBase innerObject = astObject.getInnerObject();
        assertNotNull(innerObject);
        assertTrue(innerObject instanceof ASTList);
        //
        final ASTList innerList = (ASTList) innerObject;
        assertEquals(List.class, innerList.getClazz());
        assertTrue(innerList.hasInnerObject());
        //
        assertEquals("slaves", innerList.getGetName());
        assertEquals("slaves", innerList.getSetName());
        final ASTBase innerListInnerObject = innerList.getInnerObject();
        assertNotNull(innerListInnerObject);
        assertTrue(innerListInnerObject instanceof ASTObject);
        //
        final ASTObject innerListInnerObjectObject = (ASTObject) innerListInnerObject;
        //
        assertEquals(Slave.class, innerListInnerObjectObject.getClazz());
        assertFalse(innerListInnerObjectObject.hasInnerObject());
        //
        final List<ASTBase> innerListInnerObjectAttributes = innerListInnerObjectObject.getAttributes();
        assertNotNull(innerListInnerObjectAttributes);
        assertEquals(2, innerListInnerObjectAttributes.size());
        //
        {
            final ASTBase firstAttribute = innerListInnerObjectAttributes.get(0);
            assertTrue(firstAttribute instanceof ASTPrimitive);
            assertEquals(long.class, firstAttribute.getClazz());
            assertEquals("id", firstAttribute.getGetName());
            assertEquals("id", firstAttribute.getSetName());
        }
        //
        {
            final ASTBase firstAttribute = innerListInnerObjectAttributes.get(1);
            assertTrue(firstAttribute instanceof ASTPrimitive);
            assertEquals(String.class, firstAttribute.getClazz());
            assertEquals("name", firstAttribute.getGetName());
            assertEquals("name", firstAttribute.getSetName());
        }
    }

    @Test
    public void test2() {
        final ASTParser astParser = new ASTParser();
        final ASTBase astBase = astParser.parse(new ParsingContext("", ""), Entity.class);
        assertTrue(astBase instanceof ASTObject);
        final ASTObject astObject = (ASTObject) astBase;
        assertEquals(Entity.class, astObject.getClazz());
        assertEquals("", astObject.getGetName());
        assertEquals("", astObject.getSetName());
        //
        final List<ASTBase> attributes = astObject.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        //
        {
            final ASTBase first = attributes.get(0);
            assertTrue(first instanceof ASTPrimitive);
            final ASTPrimitive firstPrimitive = ((ASTPrimitive) first);
            assertEquals(long.class, firstPrimitive.getClazz());
            assertEquals("entity_id", firstPrimitive.getGetName());
            assertEquals("id", firstPrimitive.getSetName());
        }
        {
            final ASTBase second = attributes.get(1);
            assertTrue(second instanceof ASTPrimitive);
            final ASTPrimitive firstPrimitive = ((ASTPrimitive) second);
            assertEquals(String.class, firstPrimitive.getClazz());
            assertEquals("entity_name", firstPrimitive.getGetName());
            assertEquals("name", firstPrimitive.getSetName());
        }

    }

    @Test
    public void test3() {
        final ASTParser astParser = new ASTParser();
        final ASTBase astBase = astParser.parse(new ParsingContext("", ""), entity.property.Master.class);
        assertTrue(astBase instanceof ASTObject);
        final ASTObject astObject = ((ASTObject) astBase);
        assertEquals("", astObject.getGetName());
        assertEquals("", astObject.getSetName());
        assertTrue(astObject.hasInnerObject());
        assertEquals(entity.property.Master.class, astObject.getClazz());
        //
        final List<ASTBase> attributes = astObject.getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        {
            final ASTBase attribute = attributes.get(0);
            assertNotNull(attribute);
            assertTrue(attribute instanceof ASTPrimitive);
            final ASTPrimitive astPrimitive = ((ASTPrimitive) attribute);
            assertEquals(long.class, astPrimitive.getClazz());
            assertEquals("id", astPrimitive.getGetName());
            assertEquals("id", astPrimitive.getSetName());
        }
        //
        {
            final ASTBase innerObject = astObject.getInnerObject();
            assertNotNull(innerObject);
            assertTrue(innerObject instanceof ASTList);
            final ASTList astList = ((ASTList) innerObject);
            assertEquals("slaves", astList.getGetName());
            assertEquals("slaveList", astList.getSetName());
            {
                final ASTBase astListInnerObject = astList.getInnerObject();
                assertNotNull(astListInnerObject);
                assertTrue(astListInnerObject instanceof ASTObject);
                final ASTObject astListInnerObjectObject = ((ASTObject) astListInnerObject);
                assertEquals(entity.property.Entity.class, astListInnerObjectObject.getClazz());
                assertFalse(astListInnerObjectObject.hasInnerObject());
                //
                final List<ASTBase> innerObjectObjectAttributes = astListInnerObjectObject.getAttributes();
                assertNotNull(innerObjectObjectAttributes);
                assertEquals(2, innerObjectObjectAttributes.size());
                {
                    final ASTBase first = innerObjectObjectAttributes.get(0);
                    assertTrue(first instanceof ASTPrimitive);
                    final ASTPrimitive firstASTPrimitive = (ASTPrimitive) first;
                    assertEquals("entity_id", firstASTPrimitive.getGetName());
                    assertEquals("id", firstASTPrimitive.getSetName());
                    assertEquals(long.class, firstASTPrimitive.getClazz());
                }
                {
                    final ASTBase second = innerObjectObjectAttributes.get(1);
                    assertTrue(second instanceof ASTPrimitive);
                    final ASTPrimitive secondASTPrimitive = (ASTPrimitive) second;
                    assertEquals("entity_name", secondASTPrimitive.getGetName());
                    assertEquals("name", secondASTPrimitive.getSetName());
                    assertEquals(String.class, secondASTPrimitive.getClazz());
                }
            }
        }
    }

    @Test
    public void testMap() throws Exception {
        final ASTParser astParser = new ASTParser();
        final class local { @KeyExtractor("id") public void parse() {} }
        final Annotation[] annotations = local.class.getMethod("parse").getAnnotations();
        final ASTBase result = astParser.parse(new ParsingContext("", "", annotations), new TypeToken<Map<Long, String>>() {}.getType());
        assertTrue(result instanceof ASTMap);
        final ASTMap astMap = ((ASTMap) result);
        assertEquals("", astMap.getGetName());
        assertEquals("", astMap.getSetName());
        //
        final ASTBase keyExtractor = astMap.getKey();
        assertTrue(keyExtractor instanceof ASTPrimitive);
        {
            final ASTPrimitive keyPrimitive = ((ASTPrimitive) keyExtractor);
            assertEquals("id", keyPrimitive.getGetName());
            assertEquals("", keyPrimitive.getSetName());
            assertEquals(Long.class, keyPrimitive.getClazz());
        }
        final ASTBase astMapInnerObject = astMap.getInnerObject();
        assertTrue(astMapInnerObject instanceof ASTPrimitive);
        {
            final ASTPrimitive valuePrimitive = ((ASTPrimitive) astMapInnerObject);
            assertEquals("", valuePrimitive.getGetName());
            assertEquals("", valuePrimitive.getSetName());
            assertEquals(String.class, valuePrimitive.getClazz());
        }
    }
}
