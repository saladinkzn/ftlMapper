package ru.shadam.ftlmapper.query.query;

import ru.shadam.ftlmapper.query.QueryStrategy;
import ru.shadam.ftlmapper.util.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class RawQueryStrategy implements QueryStrategy {
    private String template;

    public RawQueryStrategy(String template) {
        this.template = template;
    }

    @Override
    public PreparedStatementCreator getSql(final Object[] args) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement prepareStatement(Connection statement) throws SQLException {
                final PreparedStatement preparedStatement = statement.prepareStatement(template);
                if(args != null) {
                    for (int i = 0; i < args.length; i++) {
                        final int index = i + 1;
                        preparedStatement.setObject(index, args[i]);
                    }
                }
                return preparedStatement;
            }
        };
    }
}
