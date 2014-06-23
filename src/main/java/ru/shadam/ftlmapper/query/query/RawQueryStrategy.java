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
        String sql = template;
        // TODO: named parameters support
        int i = 1;
        if(args != null && !args.isEmpty()) {
            for(Map.Entry<String, Object> argEntry: args.entrySet()) {
                final Object value = argEntry.getValue();
                final String strValue;
                if(value instanceof String) {
                    strValue = "'" + value + "'";
                } else {
                    strValue = String.valueOf(value);
                }
                sql = sql.replace("?" + i, strValue);
                i++;
            }
        }
        return sql;
    }
}
