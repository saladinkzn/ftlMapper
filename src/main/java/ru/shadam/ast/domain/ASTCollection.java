package ru.shadam.ast.domain;

/**
 *
 */
public abstract class ASTCollection extends ASTBase {
    protected final ASTBase innerObject;

    protected ASTCollection(String getName, String setName, Class<?> collectionClass, ASTBase innerObject) {
        super(getName, setName, collectionClass);
        this.innerObject = innerObject;
    }

    public ASTBase getInnerObject() {
        return innerObject;
    }

    @Override
    public boolean hasInnerObject() {
        return true;
    }
}
