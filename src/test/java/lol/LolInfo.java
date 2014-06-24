package lol;

/**
 * Simple pojo
 */
public class LolInfo {
    private long id;
    private String name;

    public LolInfo(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LolInfo{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
