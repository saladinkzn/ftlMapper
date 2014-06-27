package ru.shadam.ftlmapper.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author sala
 */
public interface PreparedStatementCreator {
    public PreparedStatement prepareStatement(Connection statement) throws SQLException;
}
