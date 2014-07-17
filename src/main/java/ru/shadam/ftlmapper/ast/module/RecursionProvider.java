package ru.shadam.ftlmapper.ast.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;

import java.lang.reflect.Type;

/**
 *
 */
public interface RecursionProvider {
    public ASTBase parse(String getName, String setName, Type type);
}
