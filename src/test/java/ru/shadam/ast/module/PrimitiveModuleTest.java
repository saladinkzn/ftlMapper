package ru.shadam.ast.module;

import entity.Master;
import entity.Slave;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.shadam.ast.domain.ASTBase;
import ru.shadam.ast.domain.ASTPrimitive;

/**
 *
 */
public class PrimitiveModuleTest {
    private static final Class<?>[] SUPPORTED_CLASSES = {
        byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class,
        String.class
    };

    private static final Class<?>[] UNSUPPORTED_CLASSES = {
        Object.class, Slave.class, Master.class
    };

    @Test
    public void testSupports() {
        final PrimitiveModule primitiveModule = new PrimitiveModule();
        // byte
        for(Class<?> clazz: SUPPORTED_CLASSES) {
            Assert.assertTrue(primitiveModule.supports(clazz));
        }
        //
        for(Class<?> clazz: UNSUPPORTED_CLASSES) {
            Assert.assertFalse(primitiveModule.supports(clazz));
        }
    }

    @Test
    public void testParse() throws Exception {
        final PrimitiveModule primitiveModule = new PrimitiveModule();
        final RecursionProvider recursionProvider = Mockito.mock(RecursionProvider.class);
        for(Class<?> clazz: SUPPORTED_CLASSES) {
            for(String name: new String[] {"", "name"}) {
                final ASTBase string = primitiveModule.parse(name, name, clazz, recursionProvider);
                Assert.assertNotNull(string);
                Assert.assertTrue(string instanceof ASTPrimitive);
                final ASTPrimitive astPrimitive = ((ASTPrimitive) string);
                Assert.assertEquals(name, astPrimitive.getGetName());
                Assert.assertEquals(clazz, astPrimitive.getClazz());
                Mockito.verify(recursionProvider, Mockito.never()).parse(Mockito.anyString(), Mockito.anyString(), Mockito.any(Class.class));
                Mockito.reset(recursionProvider);
            }
        }
    }
}
