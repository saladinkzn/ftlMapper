package ru.shadam.extractor;

import ru.shadam.ast.domain.ASTBase;
import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.module.*;
import ru.shadam.extractor.state.SimpleState;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ResultSetExtractorFactory implements RecursionProvider {
    private final List<GeneratorModule> generatorModules;

    public ResultSetExtractorFactory() {
        generatorModules = new ArrayList<>();
        generatorModules.add(new PrimitiveGeneratorModule());
        generatorModules.add(new ListGeneratorModule());
        generatorModules.add(new ObjectGeneratorModule());
    }

    public ResultSetExtractor<?> generate(ASTBase astBase) {
        final InnerResultSetExtractor<? extends SimpleState<?>> inner = generate(astBase, null);
        return new RootResultSetExtractor(inner);
    }

    @Override
    public InnerResultSetExtractor<SimpleState<?>> generate(ASTBase astBase, ASTBase parent) {
        for(GeneratorModule generatorModule: generatorModules) {
            if(generatorModule.supports(astBase)) {
                return (InnerResultSetExtractor<SimpleState<?>>) generatorModule.extractor(astBase, parent, this);
            }
        }
        throw new IllegalArgumentException("Unsupported AST node: " + astBase);
    }
}
