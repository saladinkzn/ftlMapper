package ru.shadam.ftlmapper.ast.domain;

import java.util.Map;

/**
 *
 */
public class ASTMap extends ASTBase {
    private final ASTBase key;
    private final ASTBase innerObject;

    public ASTMap(String getName, String setName, ASTBase key, ASTBase innerObject) {
        super(getName, setName, Map.class);
        this.key = key;
        this.innerObject = innerObject;
    }

    public ASTBase getKey() {
        return key;
    }

    public ASTBase getInnerObject() {
        return innerObject;
    }

    @Override
    public boolean hasInnerObject() {
        return true;
    }
}
