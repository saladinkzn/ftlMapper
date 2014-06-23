package ru.shadam.ftlmapper.query.query;

import ru.shadam.ftlmapper.query.QueryStrategy;
import ru.shadam.ftlmapper.util.QueryManager;

import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class TemplateQueryStrategy implements QueryStrategy {
    private final QueryManager queryManager;
    private final String templateName;

    public TemplateQueryStrategy(QueryManager queryManager, String templateName) {
        this.queryManager = queryManager;
        this.templateName = templateName;
    }

    @Override
    public String getSql(Map<String, Object> args) {
        return queryManager.getQuery(templateName, args);
    }
}
