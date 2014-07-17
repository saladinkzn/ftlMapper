package ru.shadam.extractor.module;

import ru.shadam.ast.domain.ASTBase;
import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.state.SimpleState;

/**
 *
 */
public interface RecursionProvider {
    public InnerResultSetExtractor<? extends SimpleState<?>> generate(ASTBase object, ASTBase parent);
}
