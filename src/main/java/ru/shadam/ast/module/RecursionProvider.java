package ru.shadam.ast.module;

import ru.shadam.ast.domain.ASTBase;

import java.lang.reflect.Type;

/**
 *
 */
public interface RecursionProvider {
    public ASTBase parse(String getName, String setName, Type type);
}
