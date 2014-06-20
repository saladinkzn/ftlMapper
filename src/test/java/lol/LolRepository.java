package lol;

import ru.shadam.mapper.query.annotations.Mapper;
import ru.shadam.mapper.query.annotations.Param;
import ru.shadam.mapper.query.annotations.Query;

import java.util.List;

/**
 * simple repository
 */
public interface LolRepository {
    @Query("sql/lols/getAll.ftl")
    @Mapper(LolInfoMapper.class)
    public List<LolInfo> getLols(@Param("lolcount") long count);

    @Query("sql/lols/getOne.ftl")
    @Mapper(LolInfoMapper.class)
    public LolInfo getLolInfo(@Param("id") long id);
}
