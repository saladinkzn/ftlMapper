package ru.shadam.ftlmapper.annotations.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sala
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface KeyExtractor {
    /**
     * Column name or MappedType prefix
     * @return
     */
    String value();
}
