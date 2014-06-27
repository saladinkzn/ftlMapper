package ru.shadam.ftlmapper.query.query;

import ru.shadam.ftlmapper.query.QueryStrategy;
import ru.shadam.ftlmapper.util.PreparedStatementCreator;
import ru.shadam.ftlmapper.util.QueryManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class TemplateQueryStrategy implements QueryStrategy {
    private final QueryManager queryManager;
    private final String templateName;
    private final Map<Integer, String> indexToNameMap;

    public TemplateQueryStrategy(QueryManager queryManager, String templateName, Map<Integer, String> indexToNameMap) {
        this.queryManager = queryManager;
        this.templateName = templateName;
        this.indexToNameMap = indexToNameMap;
    }

    // TODO: we need a Annotation[][] in constructor or smth
    @Override
    public PreparedStatementCreator getSql(Object[] args) {
        final Map<String, Object> parameters = new HashMap<>();
        for(Map.Entry<Integer,String> argIndex : indexToNameMap.entrySet()) {
            parameters.put(argIndex.getValue(), args[argIndex.getKey()]);
        }
        final String sql = queryManager.getQuery(templateName, parameters);
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement prepareStatement(Connection statement) throws SQLException {
                return statement.prepareStatement(sql);
            }
        };
    }
}
