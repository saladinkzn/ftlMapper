package lol6;

import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Param;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public interface LolRepository {
    @Query("select id, name from lol where id = ?1 and name = ?2")
    @MappedType(LolInfo.class)
    public List<LolInfo> getOne(@Param("id") long id, @Param("name") String name);
}
