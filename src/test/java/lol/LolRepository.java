package lol;

import ru.shadam.ftlmapper.query.annotations.Mapper;
import ru.shadam.ftlmapper.query.annotations.Param;
import ru.shadam.ftlmapper.query.annotations.Template;

import java.util.List;

/**
 * simple repository
 */
public interface LolRepository {
    @Template("sql/lols/getAll.ftl")
    @Mapper(LolInfoMapper.class)
    public List<LolInfo> getLols(@Param("lolcount") long count);

    @Template("sql/lols/getOne.ftl")
    @Mapper(LolInfoMapper.class)
    public LolInfo getLolInfo(@Param("id") long id);
}
