package ru.shadam.ftlmapper.ast.module;

import java.lang.annotation.Annotation;

/**
 * @author sala
 */
public class ParsingContext {
    public final String getName;
    public final String setName;
    public final Annotation[] annotations;

    public ParsingContext(String getName, String setName) {
        this(getName, setName, new Annotation[0]);
    }

    public ParsingContext(String getName, String setName, Annotation[] annotations) {
        this.getName = getName;
        this.setName = setName;
        this.annotations = annotations;
    }
}
