package lol4;

import ru.shadam.ftlmapper.query.annotations.MappedType;
import ru.shadam.ftlmapper.query.annotations.Template;

import java.util.List;

/**
 * @author Timur Shakurov
 */
@MappedType
public interface LolRepository {
    @Template("sql/lol4/getAll.ftl")
    public List<LolInfo> getAll();
}
