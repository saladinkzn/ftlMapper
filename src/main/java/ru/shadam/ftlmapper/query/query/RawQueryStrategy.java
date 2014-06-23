package ru.shadam.ftlmapper.query.query;

import ru.shadam.ftlmapper.query.QueryStrategy;

import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class RawQueryStrategy implements QueryStrategy {
    private String template;

    public RawQueryStrategy(String template) {
        this.template = template;
    }

    @Override
    public String getSql(Map<String, Object> args) {
        // TODO: args support
        return template;
    }
}
