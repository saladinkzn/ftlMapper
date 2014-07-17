package ru.shadam.ast.module;

import ru.shadam.ast.domain.ASTBase;

import java.lang.reflect.Type;

/**
 *
 */
public interface Module {
    public abstract boolean supports(Type type);

    public abstract ASTBase parse(String getName, String setName, Type type, RecursionProvider recursionProvider);
}
