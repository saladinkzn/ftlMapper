package ru.shadam.ftlmapper.ast.module;

import ru.shadam.ftlmapper.ast.domain.ASTBase;

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
        return parse(parsingContext.getName, parsingContext.setName, type, recursionProvider);
    }

    public ASTBase parse(String getName, String setName, Type type, RecursionProvider recursionProvider) {
        throw new UnsupportedOperationException();
    }
}
