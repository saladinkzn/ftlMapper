package ru.shadam.ftlmapper.extractor.module;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTList;
import ru.shadam.ftlmapper.ast.domain.ASTPrimitive;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.simple.index.*;
import ru.shadam.ftlmapper.extractor.inner.simple.name.*;
import util.ReflectionUtil;

import java.util.Map;

import static org.junit.Assert.*;

public class PrimitiveGeneratorModuleTest {
    private static final Class<?>[] SUPPORTED_CLASSES = {
        boolean.class,
        Boolean.class,
        byte.class,
        Byte.class,
        short.class,
        Short.class,
        int.class,
        Integer.class,
        long.class,
        Long.class
    };
    public static final RecursionProvider VOID_RECURSION_PROVIDER = new RecursionProvider() {
        @Override
        public InnerResultSetExtractor<?> generate(ASTBase astBase, ASTBase parent) {
            throw new UnsupportedOperationException();
        }
    };


    @Test
    public void testSupports() throws Exception {
        final PrimitiveGeneratorModule primitiveGeneratorModule = new PrimitiveGeneratorModule();
        for(Class<?> clazz: SUPPORTED_CLASSES) {
            final ASTPrimitive astPrimitive = new ASTPrimitive("", "", clazz);
            assertTrue(primitiveGeneratorModule.supports(astPrimitive));
        }
        //
        final ASTList astList = new ASTList("",  "", new ASTPrimitive("", "", int.class));
        assertFalse(primitiveGeneratorModule.supports(astList));
        //
        // TODO:
    }

    @Test
    public void testExtractor() throws Exception {
        final PrimitiveGeneratorModule primitiveGeneratorModule = new PrimitiveGeneratorModule();
        final Class<?>[] supportedClasses = new Class<?>[] {
                long.class, Long.class,
                short.class, Short.class,
                byte.class, Byte.class,
                boolean.class, Boolean.class,
                String.class
        };
        final Map<Class<?>, Class<? extends SimpleColumnNameResultSetExtractor<?>>> returnTypeToExtractorTypeMap = ImmutableMap.<Class<?>, Class<? extends SimpleColumnNameResultSetExtractor<?>>>builder()
                .put(long.class, LongColumnNameResultSetExtractor.class)
                .put(Long.class, LongColumnNameResultSetExtractor.class)
                .put(short.class, ShortColumnNameResultSetExtractor.class)
                .put(Short.class, ShortColumnNameResultSetExtractor.class)
                .put(byte.class, ByteColumnNameResultSetExtractor.class)
                .put(Byte.class, ByteColumnNameResultSetExtractor.class)
                .put(boolean.class, BooleanColumnNameResultSetExtractor.class)
                .put(Boolean.class, BooleanColumnNameResultSetExtractor.class)
                .put(String.class, StringColumnNameResultSetExtractor.class)
                .build();
        final Map<Class<?>, Class<? extends SimpleColumnIndexResultSetExtractor<?>>> returnTypeToExtractorType2Map = ImmutableMap.<Class<?>, Class<? extends SimpleColumnIndexResultSetExtractor<?>>>builder()
                .put(long.class, LongColumnIndexResultSetExtractor.class)
                .put(Long.class, LongColumnIndexResultSetExtractor.class)
                .put(short.class, ShortColumnIndexResultSetExtractor.class)
                .put(Short.class, ShortColumnIndexResultSetExtractor.class)
                .put(byte.class, ByteColumnIndexResultSetExtractor.class)
                .put(Byte.class, ByteColumnIndexResultSetExtractor.class)
                .put(boolean.class, BooleanColumnIndexResultSetExtractor.class)
                .put(Boolean.class, BooleanColumnIndexResultSetExtractor.class)
                .put(String.class, StringColumnIndexResultSetExtractor.class)
                .build();

        for(Class<?> returnType: supportedClasses) {
            final Class<? extends SimpleColumnNameResultSetExtractor<?>> extractorType = returnTypeToExtractorTypeMap.get(returnType);
            final Class<? extends SimpleColumnIndexResultSetExtractor<?>> extractorType2 = returnTypeToExtractorType2Map.get(returnType);
            {
                final InnerResultSetExtractor<?> extractor = primitiveGeneratorModule.extractor(new ASTPrimitive("", "", returnType), null, VOID_RECURSION_PROVIDER);
                assertTrue(extractorType2.isAssignableFrom(extractor.getClass()));
                final SimpleColumnIndexResultSetExtractor simpleColumnNameResultSetExtractor = (SimpleColumnIndexResultSetExtractor) extractor;
                assertEquals(1, ReflectionUtil.getFieldValue(simpleColumnNameResultSetExtractor, "columnIndex", SimpleColumnIndexResultSetExtractor.class));
            }
            {
                final InnerResultSetExtractor<?> extractor = primitiveGeneratorModule.extractor(new ASTPrimitive("name", "", returnType), null, VOID_RECURSION_PROVIDER);
                assertTrue(extractorType.isAssignableFrom(extractor.getClass()));
                final SimpleColumnNameResultSetExtractor simpleColumnNameResultSetExtractor = (SimpleColumnNameResultSetExtractor) extractor;
                assertEquals("name", ReflectionUtil.getFieldValue(simpleColumnNameResultSetExtractor, "columnName", SimpleColumnNameResultSetExtractor.class));
            }
        }
    }
}