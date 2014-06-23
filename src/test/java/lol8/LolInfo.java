package lol8;

import ru.shadam.ftlmapper.mapper.annotations.Creator;
import ru.shadam.ftlmapper.mapper.annotations.Property;

/**
 * @author sala
 */
public class LolInfo {
    private final long id;
    private final String name;

    @Creator
    public LolInfo(@Property("id") long id, @Property("name") String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
