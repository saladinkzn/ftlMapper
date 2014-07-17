package ru.shadam.ftlmapper.extractor.module;

import ru.shadam.ftlmapper.ast.domain.ASTArray;
import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTObject;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.ArrayResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.CheckAttributesPredicate;
import ru.shadam.ftlmapper.extractor.inner.list.predicates.Predicates;
import ru.shadam.ftlmapper.extractor.state.SimpleState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sala
 */
public class ArrayGeneratorModule implements GeneratorModule {
    @Override
    public boolean supports(ASTBase astBase) {
        return astBase instanceof ASTArray;
    }

    @Override
    public ArrayResultSetExtractor<?> extractor(ASTBase astBase, ASTBase parent, RecursionProvider recursionProvider) {
        final ASTArray astArray = ((ASTArray) astBase);
        final InnerResultSetExtractor<? extends SimpleState<?>> innerObjectExtractor = recursionProvider.generate(astArray.getInnerObject(), astArray);
        if(parent == null) {
            return new ArrayResultSetExtractor(innerObjectExtractor, Predicates.alwaysFalse(), astArray.getClazz().getComponentType());
        } else {
            final List<InnerResultSetExtractor<? extends SimpleState<?>>> parentAttributesExtractors = new ArrayList<>();
            final ASTObject listParent = (ASTObject) parent;
            final List<ASTBase> attributes = listParent.getAttributes();
            for(ASTBase attribute: attributes) {
                parentAttributesExtractors.add(recursionProvider.generate(attribute, listParent));
            }
            return new ArrayResultSetExtractor(innerObjectExtractor, new CheckAttributesPredicate(parentAttributesExtractors), astArray.getClazz().getComponentType());
        }
    }
}
