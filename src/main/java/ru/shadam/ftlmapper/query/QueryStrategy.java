package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.util.PreparedStatementCreator;

/**
 * @author Timur Shakurov
 */
public interface QueryStrategy {
    public PreparedStatementCreator getSql(Object[] args);
}
