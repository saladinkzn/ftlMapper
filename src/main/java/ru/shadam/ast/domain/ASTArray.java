package ru.shadam.ast.domain;

/**
 *
 */
public class ASTArray extends ASTCollection {
    public ASTArray(String getName, String setName, Class<?> collectionClass, ASTBase innerObject) {
        super(getName, setName, collectionClass, innerObject);
    }
}
