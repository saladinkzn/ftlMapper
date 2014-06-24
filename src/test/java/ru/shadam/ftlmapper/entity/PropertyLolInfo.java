package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.mapper.annotations.Property;
import ru.shadam.ftlmapper.query.annotations.MappedType;

/**
 * @author Timur Shakurov
 */
@MappedType
public class PropertyLolInfo {
    @Property
    private long id;
    @Property
    private String name;

    public PropertyLolInfo() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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
