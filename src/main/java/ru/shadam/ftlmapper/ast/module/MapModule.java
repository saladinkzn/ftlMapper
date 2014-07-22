package ru.shadam.ftlmapper.ast.module;

import ru.shadam.ftlmapper.annotations.query.KeyExtractor;
import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author sala
 */
public class MapModule implements Module {
    @Override
    public boolean supports(Type type) {
        if(type instanceof ParameterizedType) {
            final Type rawType = ((ParameterizedType) type).getRawType();
            if(Map.class.equals(rawType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ASTBase parse(ParsingContext parsingContext, Type type, RecursionProvider recursionProvider) {
        if(!supports(type)) {
            throw new IllegalArgumentException("type: " + type + " is not supported");
        }
        //
        final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
        final Type keyType = typeArguments[0];
        final Type valueType = typeArguments[1];
        final Annotation[] annotations = parsingContext.annotations;
        KeyExtractor keyExtractor = null;
        for(Annotation annotation: annotations) {
            if(annotation instanceof KeyExtractor) {
                keyExtractor = (KeyExtractor) annotation;
            }
        }
        //
        if(keyExtractor == null) {
            throw new IllegalArgumentException("Map should have @KeyExtractor annotation");
        }
        //
        final ASTBase keyAST = recursionProvider.parse(new ParsingContext(keyExtractor.value(), "", new Annotation[0]), keyType);
        final ASTBase valueAST = recursionProvider.parse(new ParsingContext(parsingContext.getName, parsingContext.setName, new Annotation[0]), valueType);
        return new ASTMap(parsingContext.getName, parsingContext.setName, keyAST, valueAST);
    }

}
