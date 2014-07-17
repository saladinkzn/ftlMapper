package ru.shadam.extractor.module;

import ru.shadam.ast.domain.ASTBase;
import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.state.SimpleState;

/**
 *
 */
public interface GeneratorModule {
    boolean supports(ASTBase astBase);

    InnerResultSetExtractor<? extends SimpleState<?>> extractor(ASTBase astBase, ASTBase parent, RecursionProvider recursionProvider);
}
