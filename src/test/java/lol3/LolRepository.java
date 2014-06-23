package lol3;

import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public interface LolRepository {
    @Query("sql/lol3/getAll.ftl")
    @MappedType(LolInfo.class)
    public List<LolInfo> getAll();
}
