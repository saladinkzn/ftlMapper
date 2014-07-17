package ru.shadam.ftlmapper.extractor.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

/**
 *
 */
public interface RecursionProvider {
    public InnerResultSetExtractor<? extends SimpleState<?>> generate(ASTBase object, ASTBase parent);
}
