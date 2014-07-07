package ru.shadam.ftlmapper.annotations.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for query methods
 *
 * @author Timur Shakurov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Template {
    /**
     *  Template name to resolve
     */
    String value();
}
