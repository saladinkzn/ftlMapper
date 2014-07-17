package ru.shadam.ftlmapper.ast.domain;

/**
 *
 */
public class ASTArray extends ASTCollection {
    public ASTArray(String getName, String setName, Class<?> collectionClass, ASTBase innerObject) {
        super(getName, setName, collectionClass, innerObject);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ASTArray{");
        sb.append("innerObject = ").append(innerObject);
        sb.append('}');
        return sb.toString();
    }
}
