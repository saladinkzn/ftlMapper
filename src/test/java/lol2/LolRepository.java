package lol2;

import ru.shadam.mapper.query.annotations.MappedType;
import ru.shadam.mapper.query.annotations.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public interface LolRepository {
    @Query("sql/lol2/getAll.ftl")
    @MappedType(LolInfo.class)
    public List<LolInfo> getLols();
}
