package ru.shadam.mapper;

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
public @interface Query {
    /**
     *  Template name to resolve
     */
    String templateName();

    /**
     * Mapper class to use for mapping result set
     */
    Class<? extends RowMapper<?>> mapper();
}
