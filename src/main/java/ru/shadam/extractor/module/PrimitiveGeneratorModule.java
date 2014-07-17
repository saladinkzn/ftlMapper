package ru.shadam.extractor.module;

import ru.shadam.ast.domain.ASTBase;
import ru.shadam.ast.domain.ASTPrimitive;
import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.inner.simple.index.*;
import ru.shadam.extractor.inner.simple.name.*;
import ru.shadam.extractor.state.SimpleState;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PrimitiveGeneratorModule implements GeneratorModule {
    private static final Supplier<SimpleState<Boolean>> BOOLEAN_SINGLE_COLUMN_EXTRACTOR_SUPPLIER = new Supplier<SimpleState<Boolean>>() {
        @Override
        public InnerResultSetExtractor<SimpleState<Boolean>> get(String name) {
            return new BooleanColumnNameResultSetExtractor(name);
        }
    };
    private static final Supplier<SimpleState<Byte>> BYTE_SINGLE_COLUMN_EXTRACTOR_SUPPLIER = new Supplier<SimpleState<Byte>>() {
        @Override
        public InnerResultSetExtractor<SimpleState<Byte>> get(String name) {
            return new ByteColumnNameResultSetExtractor(name);
        }
    };
    private static final Supplier<SimpleState<Short>> SHORT_SINGLE_COLUMN_EXTRACTOR_SUPPLIER = new Supplier<SimpleState<Short>>() {
        @Override
        public InnerResultSetExtractor<SimpleState<Short>> get(String name) {
            return new ShortColumnNameResultSetExtractor(name);
        }
    };
    private static final Supplier<SimpleState<Integer>> INTEGER_SINGLE_COLUMN_EXTRACTOR_SUPPLIER = new Supplier<SimpleState<Integer>>() {
        @Override
        public InnerResultSetExtractor<SimpleState<Integer>> get(String name) {
            return new IntegerColumnNameResultSetExtractor(name);
        }
    };
    private static final Supplier<SimpleState<Long>> LONG_SINGLE_COLUMN_EXTRACTOR_SUPPLIER = new Supplier<SimpleState<Long>>() {
        @Override
        public InnerResultSetExtractor<SimpleState<Long>> get(String name) {
            return new LongColumnNameResultSetExtractor(name);
        }
    };
    private static final Supplier<SimpleState<String>> STRING_SINGLE_COLUMN_EXTRACTOR_SUPPLIER = new Supplier<SimpleState<String>>() {
        @Override
        public InnerResultSetExtractor<SimpleState<String>> get(String name) {
            return new StringColumnNameResultSetExtractor(name);
        }
    };
    private Map<Class<?>, InnerResultSetExtractor<? extends SimpleState<?>>> singleColumnMap;


    private static interface Supplier<T extends SimpleState<?>> {
        InnerResultSetExtractor<T> get(String name);
    }

    private Map<Class<?>, Supplier<? extends SimpleState<?>>> supplierMap;

    public PrimitiveGeneratorModule() {
        this.supplierMap = new HashMap<>();
        //
        supplierMap.put(boolean.class, BOOLEAN_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        supplierMap.put(Boolean.class, BOOLEAN_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        //
        supplierMap.put(byte.class, BYTE_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        supplierMap.put(Byte.class, BYTE_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        //
        supplierMap.put(short.class, SHORT_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        supplierMap.put(Short.class, SHORT_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        //
        supplierMap.put(int.class, INTEGER_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        supplierMap.put(Integer.class, INTEGER_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        //
        supplierMap.put(long.class, LONG_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        supplierMap.put(Long.class, LONG_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        //
        supplierMap.put(String.class, STRING_SINGLE_COLUMN_EXTRACTOR_SUPPLIER);
        //
        singleColumnMap = new HashMap<>();
        //
        singleColumnMap.put(boolean.class, new BooleanColumnIndexResultSetExtractor(1));
        singleColumnMap.put(Boolean.class, new BooleanColumnIndexResultSetExtractor(1));
        //
        singleColumnMap.put(byte.class, new ByteColumnIndexResultSetExtractor(1));
        singleColumnMap.put(Byte.class, new ByteColumnIndexResultSetExtractor(1));
        //
        singleColumnMap.put(short.class, new ShortColumnIndexResultSetExtractor(1));
        singleColumnMap.put(Short.class, new ShortColumnIndexResultSetExtractor(1));
        //
        singleColumnMap.put(int.class, new IntegerColumnIndexResultSetExtractor(1));
        singleColumnMap.put(Integer.class, new IntegerColumnIndexResultSetExtractor(1));
        //
        singleColumnMap.put(long.class, new LongColumnIndexResultSetExtractor(1));
        singleColumnMap.put(Long.class, new LongColumnIndexResultSetExtractor(1));
        //
        singleColumnMap.put(String.class, new StringColumnIndexResultSetExtractor(1));
    }

    @Override
    public boolean supports(ASTBase astBase) {
        return astBase instanceof ASTPrimitive;
    }

    @Override
    public InnerResultSetExtractor<? extends SimpleState<?>> extractor(ASTBase astBase, ASTBase parent, RecursionProvider recursionProvider) {
        final ASTPrimitive astPrimitive = ((ASTPrimitive) astBase);
        final String prefix;
        if (parent == null || parent.getGetName().isEmpty()) {
            prefix = "";
        } else {
            prefix = parent.getGetName() + "_";
        }
        if(astPrimitive.getGetName().isEmpty() && !prefix.isEmpty()) {
            throw new IllegalStateException("primitive cannot be without name if it has a prefix");
        }
        if(astPrimitive.getGetName().isEmpty()) {
            return singleColumnMap.get(astPrimitive.getClazz());
        }
        return supplierMap.get(astPrimitive.getClazz()).get(prefix + astPrimitive.getGetName());
    }
}
