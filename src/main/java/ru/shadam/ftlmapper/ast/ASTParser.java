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
        this.modules.add(new MapModule());
        this.modules.add(new CollectionModule());
        this.modules.add(new ArrayModule());
        this.modules.add(new ObjectModule());
    }

    @Override
    public ASTBase parse(ParsingContext parsingContext, Type type) {
        for(Module module: modules) {
            if(module.supports(type)) {
                return module.parse(parsingContext, type, this);
            }
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
