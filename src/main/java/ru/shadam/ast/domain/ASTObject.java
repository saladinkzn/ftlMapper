package ru.shadam.ast.domain;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ASTObject extends ASTBase {
    protected final List<ASTBase> attributes;
    protected final ASTBase innerObject;

    @Override
    public boolean hasInnerObject() {
        return innerObject != null;
    }

    public ASTObject(String getName, String setName, Class<?> clazz, List<ASTBase> attributes, ASTBase innerObject) {
        super(getName, setName, clazz);
        this.attributes = attributes;
        this.innerObject = innerObject;
    }

    public List<ASTBase> getAttributes() {
        return attributes;
    }

    public ASTBase getInnerObject() {
        return innerObject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ASTObject{");
        sb.append("getName=").append(getName);
        sb.append(", setName=").append(setName);
        sb.append(", clazz=").append(clazz.getCanonicalName());
        sb.append(", attributes=").append(attributes);
        sb.append(", innerObject=").append(innerObject);
        sb.append('}');
        return sb.toString();
    }
}
