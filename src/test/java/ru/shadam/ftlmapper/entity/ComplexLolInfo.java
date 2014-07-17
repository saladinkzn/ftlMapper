package ru.shadam.ftlmapper.entity;

import ru.shadam.annotations.Column;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author Timur Shakurov
 */
@MappedType
public class ComplexLolInfo {
    @Column("id")
    public long id;

    @Column("ch")
    public CreatorLolInfo embedded;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComplexLolInfo{");
        sb.append("id=").append(id);
        sb.append(", embedded=").append(embedded);
        sb.append('}');
        return sb.toString();
    }
}
