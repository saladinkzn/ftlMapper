package lol;

import ru.shadam.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * simple mapper
 */
public class LolInfoMapper implements RowMapper<LolInfo> {

    @Override
    public LolInfo mapRow(ResultSet resultSet) throws SQLException {
        return new LolInfo(resultSet.getLong(1), resultSet.getString(2));
    }
}
