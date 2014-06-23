package lol5;

import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public interface LolRepository {
    @Query("select id, name from lol")
    @MappedType(LolInfo.class)
    public List<LolInfo> getAll();
}
