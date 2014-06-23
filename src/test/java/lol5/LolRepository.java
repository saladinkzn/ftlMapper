package lol5;

import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Query;

import java.util.List;

/**
 * @author Timur Shakurov
 */
@MappedType
public interface LolRepository {
    @Query("select id, name from lol")
    public List<LolInfo> getAll();
}
