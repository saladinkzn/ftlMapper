package ru.shadam.ftlmapper.ast.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;

import java.lang.reflect.Type;

/**
 *
 */
public interface Module {
    public abstract boolean supports(Type type);

    public abstract ASTBase parse(String getName, String setName, Type type, RecursionProvider recursionProvider);
}
