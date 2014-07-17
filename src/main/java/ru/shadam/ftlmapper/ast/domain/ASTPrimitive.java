package ru.shadam.ftlmapper.ast.domain;

/**
 * @author Timur Shakurov
 */
public class ASTPrimitive extends ASTBase {
    public ASTPrimitive(String getName, String setName, Class<?> clazz) {
        super(getName, setName, clazz);
    }

    @Override
    public boolean hasInnerObject() {
        return false;
    }

    public String toString() {
        return "ASTPrimitive{" +
                "getName=" + getName +
                ", setName=" + setName +
                ", class=" + clazz.getCanonicalName() +
                "}";
    }
}
