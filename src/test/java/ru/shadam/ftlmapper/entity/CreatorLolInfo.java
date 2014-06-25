package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.mapper.annotations.Creator;
import ru.shadam.ftlmapper.mapper.annotations.Property;
import ru.shadam.ftlmapper.query.annotations.MappedType;

/**
 * @author Timur Shakurov
 */
@MappedType
public class CreatorLolInfo {
    private final long id;
    private final String name;

    @Creator
    public CreatorLolInfo(@Property("id") long id, @Property("name") String name) {
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