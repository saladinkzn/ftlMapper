package ru.shadam.ftlmapper.util;

/**
 * @author sala
 */
public interface Function<T, U> {
    U get(T param);
}
