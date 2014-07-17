package ru.shadam.ftlmapper.entity;

import ru.shadam.annotations.Column;
import ru.shadam.annotations.Creator;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author Timur Shakurov
 */
@MappedType
public class CreatorLolInfo {
    private final long id;
    private final String name;

    @Creator
    public CreatorLolInfo(@Column("id") long id, @Column("name") String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreatorLolInfo{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}