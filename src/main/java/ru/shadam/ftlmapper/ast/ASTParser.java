package ru.shadam.ftlmapper.ast;

import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.module.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class ASTParser implements RecursionProvider {
    private final List<Module> modules;

    public ASTParser() {
        this.modules = new ArrayList<>();
        this.modules.add(new PrimitiveModule());
        this.modules.add(new CollectionModule());
        this.modules.add(new ArrayModule());
        this.modules.add(new ObjectModule());
    }

    public ASTBase parse(String getName, String setName, Type t) {
        for(Module module: modules) {
            if(module.supports(t)) {
                return module.parse(getName, setName, t, this);
            }
        }
        throw new IllegalArgumentException("Unsupported type: " + t);
    }
}
