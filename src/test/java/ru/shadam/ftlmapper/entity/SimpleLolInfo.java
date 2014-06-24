package ru.shadam.ftlmapper.entity;

/**
 * @author Timur Shakurov
 */
public class SimpleLolInfo {
    private long id;
    private String name;

    public SimpleLolInfo(long id, String name) {
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
