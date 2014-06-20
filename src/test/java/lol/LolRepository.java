package lol;

import ru.shadam.mapper.query.annotations.Param;
import ru.shadam.mapper.query.annotations.Query;

import java.util.List;

/**
 * simple repository
 */
public interface LolRepository {
    @Query(templateName = "sql/lols/getAll.ftl", mapper = LolInfoMapper.class)
    public List<LolInfo> getLols(@Param("lolcount") long count);

    @Query(templateName = "sql/lols/getOne.ftl", mapper = LolInfoMapper.class)
    public LolInfo getLolInfo(@Param("id") long id);
}
