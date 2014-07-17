package ru.shadam.ftlmapper.ast.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTPrimitive;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 */
public class PrimitiveModule implements Module {
    private static final Class<?>[] SUPPORTED_PRIMITIVES = {
            // boolean
            boolean.class,
            Boolean.class,
            // byte
            byte.class,
            Byte.class,
            // short
            short.class,
            Short.class,
            // int
            int.class,
            Integer.class,
            // long
            long.class,
            Long.class,
            // String
            String.class
    };

    @Override
    public boolean supports(Type type) {
        if (type instanceof ParameterizedType) {
            return false;
        }
        final Class<?> clazz = (Class<?>) type;
        for (Class<?> supportedClass : SUPPORTED_PRIMITIVES) {
            if (clazz.equals(supportedClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ASTBase parse(String getName, String setName, Type type, RecursionProvider recursionProvider) {
        if(!supports(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new ASTPrimitive(getName, setName, (Class<?>) type);
    }
}
