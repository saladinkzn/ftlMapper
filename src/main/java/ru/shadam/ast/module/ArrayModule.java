package ru.shadam.ast.module;

import ru.shadam.ast.domain.ASTArray;
import ru.shadam.ast.domain.ASTBase;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 */
public class ArrayModule implements Module {
    @Override
    public boolean supports(Type type) {
        if(type instanceof ParameterizedType) {
            return false;
        }
        final Class<?> clazz = (Class<?>)type;
        return clazz.isArray();
    }

    @Override
    public ASTBase parse(String getName, String setName, Type type, RecursionProvider recursionProvider) {
        if(!supports(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        final Class<?> clazz = ((Class<?>) type);
        return new ASTArray(getName, setName, clazz, recursionProvider.parse(getName, setName, clazz.getComponentType()));
    }
}
