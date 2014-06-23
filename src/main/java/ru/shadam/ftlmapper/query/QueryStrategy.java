package ru.shadam.ftlmapper.query;

import java.util.Map;

/**
 * @author Timur Shakurov
 */
public interface QueryStrategy {
    public String getSql(Map<String, Object> args);
}
