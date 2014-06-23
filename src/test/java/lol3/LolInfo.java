package lol3;

import ru.shadam.ftlmapper.mapper.annotations.Property;

/**
 * @author Timur Shakurov
 */
public class LolInfo {
    @Property
    private long id;
    @Property
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
