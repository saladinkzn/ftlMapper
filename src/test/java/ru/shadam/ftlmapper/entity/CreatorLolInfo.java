package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.annotations.mapper.Creator;
import ru.shadam.ftlmapper.annotations.mapper.Property;
import ru.shadam.ftlmapper.annotations.query.MappedType;

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