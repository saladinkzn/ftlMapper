package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.mapper.annotations.Embedded;
import ru.shadam.ftlmapper.mapper.annotations.Property;
import ru.shadam.ftlmapper.query.annotations.MappedType;

/**
 * @author Timur Shakurov
 */
@MappedType
public class ComplexLolInfo {
    @Property("id")
    private long id;

    @Embedded("ch_")
    private CreatorLolInfo embedded;

    public long getId() {
        return id;
    }

    public CreatorLolInfo getEmbedded() {
        return embedded;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComplexLolInfo{");
        sb.append("id=").append(id);
        sb.append(", embedded=").append(embedded);
        sb.append('}');
        return sb.toString();
    }
}
