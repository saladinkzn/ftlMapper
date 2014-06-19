package lol;

import ru.shadam.mapper.Param;
import ru.shadam.mapper.Query;

import java.util.List;

/**
 * simple repository
 */
public interface LolRepository {
    @Query(templateName = "sql/lols/getAll.ftl", mapper = LolInfoMapper.class)
    public List<LolInfo> getLols(@Param("lolcount") long count);
}
