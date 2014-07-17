package ru.shadam.ftlmapper.extractor.module;

import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.Creator;
import ru.shadam.ftlmapper.ast.domain.ASTBase;
import ru.shadam.ftlmapper.ast.domain.ASTObject;
import ru.shadam.ftlmapper.extractor.inner.InnerResultSetExtractor;
import ru.shadam.ftlmapper.extractor.inner.object.*;
import ru.shadam.ftlmapper.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 *
 */
public class ObjectGeneratorModule implements GeneratorModule {
    @Override
    public boolean supports(ASTBase astBase) {
        return astBase instanceof ASTObject;
    }

    @Override
    public InnerResultSetExtractor<?> extractor(ASTBase astBase, ASTBase parent, RecursionProvider recursionProvider) {
        final ASTObject astObject = (ASTObject) astBase;
        final List<ASTBase> attributes = astObject.getAttributes();
        final Map<String, InnerResultSetExtractor<?>> attributesExtractors = new HashMap<>();
        for(ASTBase attribute: attributes) {
            attributesExtractors.put(attribute.getSetName(), recursionProvider.generate(attribute, astBase));
        }
        if(astObject.hasInnerObject()) {
            // TODO: pass attributeExtractors
            final ASTBase innerObject = astObject.getInnerObject();
            final String innerObjectName = innerObject.getSetName();
            final InnerResultSetExtractor<?> innerObjectExtractor = recursionProvider.generate(innerObject, astBase);
            attributesExtractors.put(innerObjectName, innerObjectExtractor);
        }
        final Class<?> clazz = astObject.getClazz();
        final InstanceProvider<?> instanceProvider = getInstanceProvider(clazz);
        final List<InstanceFiller<?>> instanceFillers = new ArrayList<>();
        final Set<String> attributeNames = attributesExtractors.keySet();
        for(Field field: clazz.getFields()) {
            final String fieldName = field.getName();
            if(attributeNames.contains(fieldName)) {
                instanceFillers.add(new FieldInstanceFiller<>(field, fieldName));
            }
        }
        for(Method setter: ReflectionUtil.getSetters(clazz)) {
            final String propertyName = ReflectionUtil.getPropertyName(setter);
            if(attributeNames.contains(propertyName)) {
                instanceFillers.add(new SetterInstanceFiller<>(setter, propertyName));
            }
        }
        return new ObjectResultSetExtractor<>((InstanceProvider)instanceProvider, (List)instanceFillers, attributesExtractors);
    }

    private InstanceProvider<?> getInstanceProvider(Class<?> clazz) {
        InstanceProvider<?> instanceProvider;
        final Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> creator = null;
        List<String> creatorArguments = null;
        Constructor<?> noArgs = null;
        for(Constructor<?> constructor: constructors) {
            if(constructor.isAnnotationPresent(Creator.class) && Modifier.isPublic(constructor.getModifiers())) {
                creatorArguments = new ArrayList<>();
                final Annotation[][] parametersAnnotations = constructor.getParameterAnnotations();
                ext:
                for(int i = 0; i < parametersAnnotations.length; i++) {
                    String argName = null;
                    final Annotation[] parameterAnnotations = parametersAnnotations[i];
                    for(int j = 0; j < parameterAnnotations.length; j++) {
                        if(parameterAnnotations[j] instanceof Column) {
                            final Column column = ((Column) parameterAnnotations[j]);
                            final String value = column.value();
                            if(value == null || value.isEmpty()) {
                                break ext;
                            } else {
                                argName = value;
                            }
                        } else {
                            break ext;
                        }
                    }
                    if(argName == null) {
                        break;
                    }
                    creatorArguments.add(argName);
                }
                creator = constructor;
            } else if (constructor.getParameterTypes().length == 0 && Modifier.isPublic(constructor.getModifiers())) {
                noArgs = constructor;
            }
        }
        if(creator == null && noArgs == null) {
            throw new IllegalStateException("Neither @Creator-annotated nor default constructor was found for class: " + clazz);
        }
        if(creator != null) {
            instanceProvider = new ConstructorInstanceProvider<>(creator, creatorArguments);
        } else {
            instanceProvider = new ConstructorInstanceProvider<>(noArgs, new ArrayList<String>());
        }
        return instanceProvider;
    }
}
