package ru.shadam.ftlmapper.annotations.query;

import ru.shadam.ftlmapper.mapper.RowMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Timur Shakurov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mapper {
    Class<? extends RowMapper<?>> value();
}
