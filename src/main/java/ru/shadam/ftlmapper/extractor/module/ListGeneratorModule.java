package ru.shadam.ftlmapper.extractor.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTList;
import ru.shadam.ftlmapper.ast.domain.ASTObject;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.ListResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.CheckAttributesPredicate;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.Predicates;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ListGeneratorModule implements GeneratorModule {
    @Override
    public boolean supports(ASTBase astBase) {
        return astBase instanceof ASTList;
    }

    @Override
    public ListResultSetExtractor<?> extractor(ASTBase astBase, ASTBase parent, RecursionProvider recursionProvider) {
        final ASTList astList = ((ASTList) astBase);
        final InnerResultSetExtractor<? extends SimpleState<?>> innerObjectExtractor = recursionProvider.generate(astList.getInnerObject(), astList);
        if(parent == null) {
            return new ListResultSetExtractor(innerObjectExtractor, Predicates.alwaysFalse());
        } else {
            final List<InnerResultSetExtractor<? extends SimpleState<?>>> parentAttributesExtractors = new ArrayList<>();
            final ASTObject listParent = (ASTObject) parent;
            final List<ASTBase> attributes = listParent.getAttributes();
            for(ASTBase attribute: attributes) {
                parentAttributesExtractors.add(recursionProvider.generate(attribute, listParent));
            }
            return new ListResultSetExtractor(innerObjectExtractor, new CheckAttributesPredicate(parentAttributesExtractors));
        }
    }

}
