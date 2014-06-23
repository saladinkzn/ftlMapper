package lol2;

import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Template;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public interface LolRepository {
    @Template("sql/lol2/getAll.ftl")
    @MappedType(LolInfo.class)
    public List<LolInfo> getLols();
}
