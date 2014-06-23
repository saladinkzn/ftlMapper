package lol7;

import ru.shadam.ftlmapper.query.annotations.Param;
import ru.shadam.ftlmapper.query.annotations.Query;

/**
 * @author Timur Shakurov
 */
public interface LolRepository {
    @Query("select id from lol where name = ?1")
    public long getIdByName(@Param("name") String name);
}
