package ru.shadam.ftlmapper.extractor.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

/**
 *
 */
public interface GeneratorModule {
    boolean supports(ASTBase astBase);

    InnerResultSetExtractor<? extends SimpleState<?>> extractor(ASTBase astBase, ASTBase parent, RecursionProvider recursionProvider);
}
