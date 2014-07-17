package ru.shadam.ast.domain;

/**
 * @author Timur Shakurov
 */
public abstract class ASTBase {
    protected final String getName;
    protected final String setName;
    protected final Class<?> clazz;

    public ASTBase(String getName, String setName, Class<?> clazz) {
        this.getName = getName;
        this.setName = setName;
        this.clazz = clazz;
    }

    public String getGetName() {
        return getName;
    }

    public String getSetName() {
        return setName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public abstract boolean hasInnerObject();
}
