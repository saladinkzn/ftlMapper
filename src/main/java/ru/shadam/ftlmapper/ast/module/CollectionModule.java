package ru.shadam.ftlmapper.ast.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTList;
import ru.shadam.ftlmapper.ast.domain.ASTSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class CollectionModule implements Module {
    @Override
    public boolean supports(Type type) {
        if(type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = ((ParameterizedType) type);
            final Type rawType = parameterizedType.getRawType();
            if(List.class.equals(rawType) || Set.class.equals(rawType)) {
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                assert actualTypeArguments.length == 1;
                return true;
            }
        }
        return false;
    }

    @Override
    public ASTBase parse(ParsingContext parsingContext, Type type, RecursionProvider recursionProvider) {
        if(!supports(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        final ParameterizedType parameterizedType = ((ParameterizedType) type);
        final Type rawType = parameterizedType.getRawType();
        final Type[] typeArguments = parameterizedType.getActualTypeArguments();
        assert typeArguments.length == 1;
        final ASTBase innerObject = recursionProvider.parse(
                new ParsingContext(parsingContext.getName, parsingContext.setName, new Annotation[0]),
                typeArguments[0]
        );
        if(List.class.equals(rawType)) {
            return new ASTList(parsingContext.getName, parsingContext.setName, innerObject);
        } else if (Set.class.equals(rawType)) {
            return new ASTSet(parsingContext.getName, parsingContext.setName, innerObject);
        }
        throw new IllegalStateException("Unsupported rawType: " + rawType);
    }

}
