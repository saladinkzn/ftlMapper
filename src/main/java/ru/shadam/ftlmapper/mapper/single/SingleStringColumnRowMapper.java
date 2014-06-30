package ru.shadam.ftlmapper.mapper.single;

import ru.shadam.ftlmapper.mapper.ResultSetWrapper;

import java.sql.SQLException;

/**
 * @author Timur Shakurov
 */
public class SingleStringColumnRowMapper implements ru.shadam.ftlmapper.mapper.RowMapper<String> {
    private MapperType mapperType;
    private int columnIndex;
    private String columnName;

    public SingleStringColumnRowMapper() {
        this(1);
    }

    public SingleStringColumnRowMapper(int columnIndex) {
        this.mapperType = MapperType.INDEX;
        this.columnIndex = columnIndex;
    }

    public SingleStringColumnRowMapper(String columnName) {
        this.mapperType = MapperType.NAME;
        this.columnName = columnName;
    }

    @Override
    public String mapRow(ResultSetWrapper resultSet) throws SQLException {
        switch (mapperType) {
            case INDEX:
                return resultSet.getString(columnIndex);
            case NAME:
                return resultSet.getString(columnName);
            default:
                throw new IllegalStateException("Unsupported mapperType: " + mapperType);
        }
    }
}
