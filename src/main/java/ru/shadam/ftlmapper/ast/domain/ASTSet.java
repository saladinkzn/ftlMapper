package ru.shadam.ftlmapper.ast.domain;

import java.util.Set;

/**
 *
 */
public class ASTSet extends ASTCollection {
    public ASTSet(String getName, String setName, ASTBase innerObject) {
        super(getName, setName, Set.class, innerObject);
    }
}
