package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.mapper.annotations.Creator;
import ru.shadam.ftlmapper.mapper.annotations.Embedded;
import ru.shadam.ftlmapper.mapper.annotations.Property;
import ru.shadam.ftlmapper.query.annotations.MappedType;

/**
 * @author Timur Shakurov
 */
@MappedType
public class CreatorComplexLolInfo {
    private final long id;

    private final CreatorLolInfo embedded;

    @Creator
    public CreatorComplexLolInfo(@Property("id") long id, @Embedded("ch_") CreatorLolInfo embedded) {
        this.id = id;
        this.embedded = embedded;
    }

    public long getId() {
        return id;
    }

    public CreatorLolInfo getEmbedded() {
        return embedded;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreatorComplexLolInfo{");
        sb.append("id=").append(id);
        sb.append(", embedded=").append(embedded);
        sb.append('}');
        return sb.toString();
    }
}
