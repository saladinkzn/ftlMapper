package ru.shadam.ftlmapper.ast.domain;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ASTList extends ASTCollection {
    public ASTList(String getName, String setName, ASTBase innerObject) {
        super(getName, setName, List.class, innerObject);
    }

    @Override
    public String toString() {
        return "ASTList{ " +
            "innerObject=" + innerObject +
            "}";
    }
}
