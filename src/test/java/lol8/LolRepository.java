package lol8;

import ru.shadam.ftlmapper.query.annotations.Query;

import java.util.Set;

/**
 * @author sala
 */
public interface LolRepository {
    @Query("select id, name from lol order by id asc")
    public Set<Long> getIds();

    @Query("select name from lol order by id asc")
    public Set<String> getNames();
}
