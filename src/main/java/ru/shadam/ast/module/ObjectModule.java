package ru.shadam.ast.module;

import ru.shadam.annotations.Column;
import ru.shadam.ast.domain.ASTBase;
import ru.shadam.ast.domain.ASTObject;
import ru.shadam.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ObjectModule implements Module {
    @Override
    public boolean supports(Type type) {
        return !(type instanceof ParameterizedType) && !(type.equals(Object.class));
    }

    @Override
    public ASTBase parse(String getName, String setName, Type type, RecursionProvider recursionProvider) {
        if(!supports(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        //
        final Class<?> clazz = (Class<?>) type;
        // ASTObject:
        final List<ASTBase> attributes = new ArrayList<>();
        final ASTBase[] innerObject = new ASTBase[1];
        final Field[] fields = clazz.getFields();
        for(Field field: fields) {
            final String fieldGetName;
            final String fieldSetName = field.getName();
            {
                final Column column = field.getAnnotation(Column.class);
                if(column == null) {
                    fieldGetName = field.getName();
                } else {
                    fieldGetName = column.value();
                }
            }
            final ASTBase astBase = recursionProvider.parse(fieldGetName, fieldSetName, field.getGenericType());
            if(!astBase.hasInnerObject()) {
                attributes.add(astBase);
            } else {
                if(innerObject[0] != null) {
                    throw new IllegalStateException("already have an inner object");
                } else {
                    innerObject[0] = astBase;
                }
            }
        }
        //
        final List<Method> setters = ReflectionUtil.getSetters(clazz);
        for(Method method: setters) {
            final String propertyName = ReflectionUtil.getPropertyName(method);
            final String propertySetName = propertyName;
            final String propertyGetName;
            {
                final Column column = method.getAnnotation(Column.class);
                if(column == null) {
                    propertyGetName = propertyName;
                } else {
                    propertyGetName = column.value();
                }
            }
            final ASTBase astBase = recursionProvider.parse(propertyGetName, propertySetName, method.getParameterTypes()[0]);
            if(!astBase.hasInnerObject()) {
                attributes.add(astBase);
            } else if(innerObject[0] != null) {
                throw new IllegalStateException("already have an inner object");
            } else {
                innerObject[0] = astBase;
            }
        }
        return new ASTObject(getName, setName, clazz, attributes, innerObject[0]);
    }
}
