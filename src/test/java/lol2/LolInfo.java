package lol2;

import ru.shadam.mapper.mapper.annotations.Property;

/**
 * @author Timur Shakurov
 */
public class LolInfo {
    @Property("id")
    private long id;

    @Property("name")
    private String name;

    public LolInfo() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LolInfo{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
